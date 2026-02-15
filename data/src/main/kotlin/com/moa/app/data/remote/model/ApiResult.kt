package com.moa.app.data.remote.model

sealed interface ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>

    data class Error(
        val code: String,
        val message: String
    ) : ApiResult<Nothing>
}
