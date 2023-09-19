package com.vt.vt.core.data.source.network

import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

class NetworkError(var error: Throwable) : Throwable() {
    val errorMessage: String?
        get() = error.message

    val isNetworkError: Boolean
        get() = error is IOException

    val isHttpError: Boolean
        get() = error is HttpException

    val isAuthFailure: Boolean
        get() = isHttpError && (error as HttpException).code() == HttpURLConnection.HTTP_UNAUTHORIZED

    val isAuthForbidden: Boolean
        get() = isHttpError && (error as HttpException).code() == HttpURLConnection.HTTP_FORBIDDEN

    val isBadRequest: Boolean
        get() = isHttpError && (error as HttpException).code() == HttpURLConnection.HTTP_BAD_REQUEST

    val httpErrorCode: String
        get() = if (isHttpError) (error as HttpException).code().toString() else ""

    val isInternalServerError: Boolean
        get() = isHttpError && (error as HttpException).code() == HttpURLConnection.HTTP_INTERNAL_ERROR

    val isUnprocessError: Boolean
        get() = isHttpError && (error as HttpException).code() == HTTP_UNPROCESS_ENTITY

    val tokenIsExpired: Boolean
        get() = isHttpError && (error as HttpException).code() == HttpURLConnection.HTTP_FORBIDDEN

    companion object {
        private const val HTTP_UNPROCESS_ENTITY = 422
    }
}
