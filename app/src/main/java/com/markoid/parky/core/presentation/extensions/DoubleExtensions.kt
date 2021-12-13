package com.markoid.parky.core.presentation.extensions

fun String.toDouble(defaultValue: Double): Double = try {
    toDouble()
} catch (error: Throwable) {
    defaultValue
}
