package com.markoid.parky.core.presentation.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> List<T>.serialize(): String = Gson().toJson(this)

inline fun <reified T> String.deserialize(): List<T> = try {
//    Gson().fromJson(this, Array<T>::class.java).toList()
    val type = object : TypeToken<List<T>>() {}.type
    Gson().fromJson<List<T>>(this, type).toList()
} catch (error: Throwable) {
    error.printStackTrace()
    emptyList()
}
