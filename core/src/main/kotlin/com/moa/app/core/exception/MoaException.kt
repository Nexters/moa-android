package com.moa.app.core.exception

sealed class MoaException(message: String? = null, cause: Throwable? = null) : Exception(message, cause)

class ServerException(cause: Throwable? = null) : MoaException("서버 오류", cause)

class NetworkException(cause: Throwable? = null) : MoaException("네트워크 오류", cause)

data class ApiErrorException(
    val code: String,
    val serverMessage: String
) : MoaException("[$code] $serverMessage")
