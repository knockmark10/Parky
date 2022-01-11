package com.markoid.parky.core.presentation.modules

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.markoid.parky.R
import com.markoid.parky.core.domain.interceptors.NetworkInterceptor
import com.markoid.parky.core.presentation.annotations.AppOkHttp
import com.markoid.parky.core.presentation.serializers.DateTimeSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.DateTime
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val TIMEOUT = 10L

@Module
@InstallIn(SingletonComponent::class)
class NetModule {

    @Provides
    @Singleton
    fun providesGson(): Gson = GsonBuilder()
        .registerTypeAdapter(DateTime::class.java, DateTimeSerializer())
        .create()

    @Provides
    @Singleton
    fun provideOkHttpCache(application: Application): Cache {
        val cacheSize = 10L * 1024L * 1024L // 10 MiB
        return Cache(application.cacheDir, cacheSize)
    }

    @Provides
    @Singleton
    fun provideNetworkInterceptor(
        @ApplicationContext context: Context
    ): NetworkInterceptor = NetworkInterceptor(context)

    @AppOkHttp
    @Provides
    @Singleton
    fun providesOkHttpClient(
        cache: Cache,
        networkInterceptor: NetworkInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor(networkInterceptor)
        .cache(cache)
        .build()

    @Provides
    @Singleton
    fun providesRetrofit(
        gson: Gson,
        @AppOkHttp okHttpClient: OkHttpClient,
        res: Resources
    ): Retrofit = Retrofit.Builder()
        .baseUrl(res.getString(R.string.server_preference_url))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()
}
