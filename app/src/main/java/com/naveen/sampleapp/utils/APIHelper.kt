package com.naveen.sampleapp.utils

import com.google.gson.JsonSyntaxException
import org.json.JSONObject
import retrofit2.Response


sealed class ApiResult<out T : Any?> {
    data class Success<out T : Any?>(val data: T?) : ApiResult<T>()

    data class ApiError(val exception: Exception?) : ApiResult<Nothing>()
}


suspend fun <T : Any> handleApi(
    call: suspend () -> Response<T>,
    errorMessage: String = "Some errors occurred, Please try again later"
): ApiResult<T> {
    try {
        val response = call()
        if (response.isSuccessful) {
            response.body()?.let {
                return ApiResult.Success(it)
            }
        }
        response.errorBody()?.let {
            return try {
                val errorString = it.string()
                val errorObject = JSONObject(errorString)
                ApiResult.ApiError(
                    RuntimeException(if (errorObject.has("message")) errorObject.getString("message") else "Error occurred, Try again Later")
                )
            } catch (ignored: JsonSyntaxException) {
                ignored.printStackTrace()
                ApiResult.ApiError(ignored)
            }
        }
        return ApiResult.ApiError(RuntimeException(errorMessage))
    } catch (e: Exception) {
        e.printStackTrace()
        return ApiResult.ApiError(e)
    }
}