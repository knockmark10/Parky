package com.markoid.parky.position.domain.interceptors

import android.content.res.Resources
import com.markoid.parky.R
import com.markoid.parky.core.presentation.extensions.mapTo
import com.markoid.parky.position.presentation.utils.PositionConstants
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import kotlin.random.Random

class PositionStackInterceptor(resources: Resources) : Interceptor {

    private val pool = listOf(
        resources.getString(R.string.geocoding_api_key_one),
        resources.getString(R.string.geocoding_api_key_two),
    )

    private val apiKey: String
        get() = Random.nextInt(0, pool.size).mapTo { pool[it] }

    private val endpointsWithApiKey = listOf(
        PositionConstants.POSITION_STACK_REVERSE_GEOCODING,
        PositionConstants.POSITION_STACK_FORWARD_GEOCODING,
    )

    override fun intercept(chain: Interceptor.Chain): Response =
        if (isApiKeyRequired(chain.request())) {
            val urlBuilder = chain.request().url.newBuilder()
            urlBuilder.addQueryParameter("access_key", apiKey)
            val request: Request = chain.request().newBuilder().url(urlBuilder.build()).build()
            chain.proceed(request)
        } else {
            chain.proceed(chain.request().newBuilder().build())
        }

    private fun isApiKeyRequired(request: Request): Boolean {
        val endpoint = request.url.toUrl().path.toString()
        return endpointsWithApiKey.firstOrNull { it == endpoint } != null
    }
}
