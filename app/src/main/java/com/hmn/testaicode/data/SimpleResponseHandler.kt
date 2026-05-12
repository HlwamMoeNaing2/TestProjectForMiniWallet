package com.hmn.testaicode.data

sealed class SimpleResponseHandler<out T> {
    data class Success<out T>(val data: T) : SimpleResponseHandler<T>()
    data class Error(val exception: Throwable) : SimpleResponseHandler<Nothing>()
}