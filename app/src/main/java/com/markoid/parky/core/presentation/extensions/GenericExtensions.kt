package com.markoid.parky.core.presentation.extensions

infix fun <T, R> T.mapTo(func: (T) -> R): R = func(this)
