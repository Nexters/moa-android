package com.moa.app.data.remote.adapter

import com.moa.app.data.remote.model.ApiResponse
import com.moa.app.data.remote.model.ApiResult
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResultCallAdapterFactory() : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != ApiResult::class.java) {
            return null
        }

        check(returnType is ParameterizedType) {
            "ApiResult must have generic type (e.g., ApiResult<ResponseBody>)"
        }

        val contentType = getParameterUpperBound(0, returnType)

        val apiResponseType = object : ParameterizedType {
            override fun getRawType(): Type = ApiResponse::class.java
            override fun getActualTypeArguments(): Array<Type> = arrayOf(contentType)
            override fun getOwnerType(): Type? = null
        }

        return ResultCallAdapter<Any>(apiResponseType)
    }

    private class ResultCallAdapter<T>(
        private val apiResponseType: Type,
    ) : CallAdapter<ApiResponse<T>, Call<ApiResult<T>>> {

        override fun responseType(): Type = apiResponseType

        override fun adapt(call: Call<ApiResponse<T>>): Call<ApiResult<T>> {
            return ResultCall(call)
        }
    }

    private class ResultCall<T>(
        private val delegate: Call<ApiResponse<T>>,
    ) : Call<ApiResult<T>> {

        override fun execute(): Response<ApiResult<T>> {
            val response = delegate.execute()
            return Response.success(response.toApiResult())
        }

        override fun enqueue(callback: Callback<ApiResult<T>>) {
            delegate.enqueue(object : Callback<ApiResponse<T>> {
                override fun onResponse(
                    call: Call<ApiResponse<T>>,
                    response: Response<ApiResponse<T>>
                ) {
                    val apiResult = response.toApiResult()
                    callback.onResponse(
                        this@ResultCall,
                        Response.success(apiResult)
                    )
                }

                override fun onFailure(call: Call<ApiResponse<T>>, t: Throwable) {
                    val errorResult = ApiResult.Error(
                        code = "NETWORK_ERROR",
                        message = t.message ?: "Unknown network error"
                    )
                    callback.onResponse(
                        this@ResultCall,
                        Response.success(errorResult)
                    )
                }
            })
        }

        private fun Response<ApiResponse<T>>.toApiResult(): ApiResult<T> {
            val apiResponse = body()

            if (!isSuccessful || apiResponse == null) {
                return ApiResult.Error(
                    code = "HTTP_ERROR",
                    message = message() ?: "HTTP Error"
                )
            }

            return if (apiResponse.content == null) {
                ApiResult.Error(
                    code = apiResponse.code,
                    message = apiResponse.message
                )
            } else {
                ApiResult.Success(apiResponse.content)
            }
        }

        override fun isExecuted(): Boolean = delegate.isExecuted
        override fun cancel() = delegate.cancel()
        override fun isCanceled(): Boolean = delegate.isCanceled
        override fun request(): Request = delegate.request()
        override fun timeout(): Timeout = delegate.timeout()
        override fun clone(): Call<ApiResult<T>> = ResultCall(delegate.clone())
    }
}
