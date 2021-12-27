package com.markoid.parky.settings.presentation.managers

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes
import com.markoid.parky.core.presentation.extensions.getDouble
import com.markoid.parky.core.presentation.extensions.putDouble

abstract class AbstractPreferences(
    private val context: Context,
    val preferences: SharedPreferences
) {

    fun setPreference(
        @StringRes key: Int,
        value: Any
    ) {
        when (value) {
            is Int -> preferences.edit().putInt(getKey(key), value).apply()
            is Float -> preferences.edit().putFloat(getKey(key), value).apply()
            is Long -> preferences.edit().putLong(getKey(key), value).apply()
            is Boolean -> preferences.edit().putBoolean(getKey(key), value).apply()
            is String -> preferences.edit().putString(getKey(key), value).apply()
            is Double -> preferences.edit().putDouble(getKey(key), value).apply()
            else -> throw UnsupportedOperationException("Value type not supported. If required, you need to manually add it on AbstractPreferences class.")
        }
    }

    inline fun <reified T : Any> getPreference(
        @StringRes key: Int,
        defaultValue: T? = null
    ): T = when (T::class) {
        Int::class -> preferences.getInt(getKey(key), defaultValue as? Int ?: -1) as T
        Float::class -> preferences.getFloat(getKey(key), defaultValue as? Float ?: -1f) as T
        Long::class -> preferences.getLong(getKey(key), defaultValue as? Long ?: -1L) as T
        Boolean::class ->
            preferences.getBoolean(getKey(key), defaultValue as? Boolean ?: false) as T
        String::class ->
            preferences.getString(getKey(key), defaultValue as? String ?: "") as T
        Double::class ->
            preferences.getDouble(getKey(key), defaultValue as? Double ?: 0.0) as T
        else ->
            throw UnsupportedOperationException("Value type not supported. You need to specify default value with proper type. If value is not supported, you need to manually add it on AbstractPreferences class.")
    }

    fun getKey(resId: Int): String = context.getString(resId)
}
