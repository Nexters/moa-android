package com.moa.salary.app.presentation.manager

import com.moa.salary.app.core.exception.ApiErrorException
import com.moa.salary.app.core.exception.MoaException
import com.moa.salary.app.core.exception.NetworkException
import com.moa.salary.app.core.exception.ServerException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
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

    private fun mapHttpException(exception: HttpException): Exception {
        val httpCode = exception.code()
        val errorBody = exception.response()?.errorBody()?.string()

        return when {
            httpCode >= 500 -> ServerException(exception)

            httpCode >= 400 -> {
                val errorPair = errorBody?.let { parseErrorCodeAndMessage(it) }
                if (errorPair != null) {
                    ApiErrorException(errorPair.first, errorPair.second)
                } else {
                    exception
                }
            }

            else -> exception
        }
    }

    private fun parseErrorCodeAndMessage(jsonString: String): Pair<String, String>? {
        return try {
            val jsonElement = json.parseToJsonElement(jsonString)
            val code = jsonElement.jsonObject["code"]?.jsonPrimitive?.content
            val message = jsonElement.jsonObject["message"]?.jsonPrimitive?.content

            if (code != null && message != null) {
                Pair(code, message)
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }
}