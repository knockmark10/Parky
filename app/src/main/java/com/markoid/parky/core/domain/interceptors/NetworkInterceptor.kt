package com.markoid.parky.core.domain.interceptors

import android.content.Context
import com.markoid.parky.core.domain.exceptions.ServerException
import com.markoid.parky.core.presentation.managers.NetworkManager
import okhttp3.Interceptor
import okhttp3.Response

class NetworkInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        if (NetworkManager.isNetworkConnected(context).not()) {
            throw ServerException()
        } else {
            val builder = chain.request().newBuilder()
            chain.proceed(builder.build())
        }
}
