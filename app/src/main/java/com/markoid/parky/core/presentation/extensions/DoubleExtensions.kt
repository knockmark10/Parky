package com.markoid.parky.core.presentation.extensions

fun String.toDouble(defaultValue: Double): Double = try {
    toDouble()
} catch (error: Throwable) {
    defaultValue
}

fun Double.asMoney(): String = String.format("$%.2f", this)
