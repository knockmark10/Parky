package com.markoid.parky.core.data.enums

sealed class DataState<T> {
    class Data<T>(val data: T) : DataState<T>()
    class Error<T>(val error: String) : DataState<T>()
}
