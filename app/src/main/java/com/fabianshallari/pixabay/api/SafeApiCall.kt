package com.fabianshallari.pixabay.api

import kotlin.coroutines.cancellation.CancellationException

inline fun <T> safeApiCall(apiCall: () -> T): ApiResult<T> = try {
    ApiResult.Success(apiCall())
} catch (throwable: Throwable) {
    if (throwable is CancellationException) {
        throw throwable
    }
    ApiResult.Failure(throwable)
}