package com.example.airpollutionmonitor.util

import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(apiCall: suspend () -> T): ApiResultWrapper<T> {

        try {
            return ApiResultWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> ApiResultWrapper.NetworkError
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = throwable.response()?.errorBody()?.toString() ?: "No Error body"
                    return ApiResultWrapper.Failure(code, errorResponse)
                }
                else -> {
                    return ApiResultWrapper.Failure(null, null)
                }
            }
        }
        return ApiResultWrapper.Failure(null, null)
}

sealed class ApiResultWrapper<out T> {
    data class Success<out T>(val value: T) : ApiResultWrapper<T>()
    data class Failure(val code: Int? = null, val error: String? = null) : ApiResultWrapper<Nothing>()
    object NetworkError : ApiResultWrapper<Nothing>()
}