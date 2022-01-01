package com.markoid.parky.core.presentation.extensions

import java.util.Locale

object LocaleExtensions {

    fun getLocaleForLanguage(language: String): Locale = when (language) {
        "es" -> Locale("es", "MX")
        else -> Locale.ENGLISH
    }

    fun getLocaleIndex(language: String): Int = when (language) {
        "es" -> 1
        else -> 0
    }
}
