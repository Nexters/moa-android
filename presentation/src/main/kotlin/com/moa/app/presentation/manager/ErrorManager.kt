package com.moa.app.presentation.manager

import com.moa.app.core.exception.ApiErrorException
import com.moa.app.core.exception.MoaException
import com.moa.app.core.exception.NetworkException
import com.moa.app.core.exception.ServerException
import com.moa.app.data.remote.model.response.ApiResponse
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorManager {
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
        encodeDefaults = true
    }

    fun map(throwable: Throwable): Exception {
        return when (throwable) {
            is HttpException -> mapHttpException(throwable)

            is SocketTimeoutException -> NetworkException(throwable)
            is UnknownHostException -> NetworkException(throwable)
            is IOException -> NetworkException(throwable)

            is MoaException -> throwable

            else -> Exception(throwable)
        }
    }

    private fun mapHttpException(exception: HttpException): MoaException {
        val httpCode = exception.code()
        val errorBody = exception.response()?.errorBody()?.string()

        return when {
            httpCode >= 500 -> ServerException(exception)

            httpCode >= 400 -> {
                val apiResponse = errorBody?.let { parseApiResponse(it) }
                if (apiResponse != null) {
                    ApiErrorException(apiResponse.code, apiResponse.message)
                } else {
                    ServerException(exception)
                }
            }

            else -> ServerException(exception)
        }
    }

    private fun parseApiResponse(jsonString: String): ApiResponse<Any?>? {
        return try {
            json.decodeFromString<ApiResponse<Any?>>(jsonString)
        } catch (_: Exception) {
            null
        }
    }
}