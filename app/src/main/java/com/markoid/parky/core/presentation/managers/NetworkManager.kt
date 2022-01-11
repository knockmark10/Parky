package com.markoid.parky.core.presentation.managers

import android.content.Context
import android.os.Build
import com.markoid.parky.core.presentation.extensions.activeNetworkCapabilities
import com.markoid.parky.core.presentation.extensions.connectivityManager
import com.markoid.parky.core.presentation.extensions.hasConnection

object NetworkManager {

    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager = context.connectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities = connectivityManager.activeNetworkCapabilities
            capabilities != null && capabilities.hasConnection
        } else {
            val activeNetwork = connectivityManager.activeNetworkInfo
            activeNetwork != null && activeNetwork.hasConnection
        }
    }
}
