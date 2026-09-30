package com.moa.salary.app.data.remote.interceptor

import com.moa.salary.app.data.remote.api.AuthService
import com.moa.salary.app.data.remote.model.request.RefreshTokenRequest
import com.moa.salary.app.data.repository.TokenRepository
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val authService: Lazy<AuthService>,
) : Authenticator {

    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {
        // refresh 요청은 mutex 안에서 실행되므로 여기서 다시 잠그면 데드락이 난다.
        if (response.request.url.encodedPath == AuthService.REFRESH_PATH) return null

        val failedAccessToken = response.request.header(AUTHORIZATION)
            ?.removePrefix(BEARER_PREFIX)
            ?: return null

        return runBlocking {
            if (response.priorResponse != null) {
                mutex.withLock { expireIfCurrent(failedAccessToken) }
                return@runBlocking null
            }

            val newAccessToken = mutex.withLock { refresh(failedAccessToken) }
                ?: return@runBlocking null

            response.request.newBuilder()
                .header(AUTHORIZATION, "$BEARER_PREFIX$newAccessToken")
                .build()
        }
    }

    private suspend fun refresh(failedAccessToken: String): String? {
        val currentAccessToken = tokenRepository.getAccessToken() ?: return null
        if (currentAccessToken != failedAccessToken) return currentAccessToken

        val refreshToken = tokenRepository.getRefreshToken()
        if (refreshToken == null) {
            tokenRepository.expireSession()
            return null
        }

        return try {
            val tokens = authService.get().refresh(RefreshTokenRequest(refreshToken))
            tokenRepository.saveTokens(tokens.accessToken, tokens.refreshToken)
            tokens.accessToken
        } catch (e: HttpException) {
            if (e.code() == HTTP_UNAUTHORIZED) tokenRepository.expireSession()
            null
        } catch (_: IOException) {
            null
        }
    }

    private suspend fun expireIfCurrent(failedAccessToken: String) {
        if (tokenRepository.getAccessToken() == failedAccessToken) {
            tokenRepository.expireSession()
        }
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"
        private const val BEARER_PREFIX = "Bearer "
        private const val HTTP_UNAUTHORIZED = 401
    }
}
