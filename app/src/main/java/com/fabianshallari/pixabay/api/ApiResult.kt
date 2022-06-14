package com.fabianshallari.pixabay.api

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Failure(@JvmField val error: Throwable) : ApiResult<Nothing>()
}