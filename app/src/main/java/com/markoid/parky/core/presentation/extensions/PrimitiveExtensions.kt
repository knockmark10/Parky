package com.markoid.parky.core.presentation.extensions

fun Long.toDigits(): String =
    if (toString().length > 1) toString()
    else StringBuilder().append("0").append(this).toString()
