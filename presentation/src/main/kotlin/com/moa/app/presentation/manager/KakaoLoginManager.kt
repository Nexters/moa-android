package com.moa.app.presentation.manager

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object KakaoLoginManager {
    private val kakaoApiClient = UserApiClient.instance

    suspend fun loginWithKakao(context: Context): String =
        suspendCancellableCoroutine { continuation ->
            if (kakaoApiClient.isKakaoTalkLoginAvailable(context)) {
                kakaoApiClient.loginWithKakaoTalk(context) { token, error ->
                    setLoginResult(token, error, continuation)
                }
            } else {
                kakaoApiClient.loginWithKakaoAccount(context) { token, error ->
                    setLoginResult(token, error, continuation)
                }
            }
        }

    private fun setLoginResult(
        token: OAuthToken?,
        error: Throwable?,
        continuation: CancellableContinuation<String>,
    ) {
        when {
            error != null -> continuation.resumeWithException(
                Exception(
                    error.message ?: "카카오 계정으로 로그인 실패"
                )
            )

            token != null -> continuation.resume(token.accessToken)
            else -> continuation.resumeWithException(Exception("카카오 계정으로 로그인 실패"))
        }
    }

    suspend fun logoutKakao() = suspendCancellableCoroutine { continuation ->
        kakaoApiClient.logout { error ->
            if (error != null) {
                continuation.resumeWithException(Exception(error.message))
            } else {
                continuation.resume("Success")
            }
        }
    }
}