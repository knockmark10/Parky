package com.markoid.parky.core.presentation.extensions

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import androidx.annotation.RequiresApi

@Suppress("deprecation")
val NetworkInfo.isWifi
    get() = type == ConnectivityManager.TYPE_WIFI

@Suppress("deprecation")
val NetworkInfo.isMobile
    get() = type == ConnectivityManager.TYPE_MOBILE

@Suppress("deprecation")
val NetworkInfo.hasConnection
    get() = isWifi || isMobile

val ConnectivityManager.activeNetworkCapabilities: NetworkCapabilities?
    @RequiresApi(Build.VERSION_CODES.M)
    get() = getNetworkCapabilities(activeNetwork)

val NetworkCapabilities.hasConnection
    get() = hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
