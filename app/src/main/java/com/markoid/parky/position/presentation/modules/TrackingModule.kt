package com.markoid.parky.position.presentation.modules

import android.content.Context
import android.content.res.Resources
import android.os.Looper
import com.google.gson.Gson
import com.markoid.parky.R
import com.markoid.parky.core.presentation.annotations.AppOkHttp
import com.markoid.parky.position.data.datasources.TrackingDataSource
import com.markoid.parky.position.data.datasources.TrackingDataSourceImpl
import com.markoid.parky.position.data.repositories.TrackingRepository
import com.markoid.parky.position.data.repositories.TrackingRepositoryImpl
import com.markoid.parky.position.data.services.BigDataCloudService
import com.markoid.parky.position.data.services.PositionStackService
import com.markoid.parky.position.domain.interceptors.PositionStackInterceptor
import com.markoid.parky.position.presentation.annotations.BigDataCloudServer
import com.markoid.parky.position.presentation.annotations.PositionStackOkHttp
import com.markoid.parky.position.presentation.annotations.PositionStackServer
import com.markoid.parky.position.presentation.managers.PositionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TIMEOUT = 120L

@Module
@InstallIn(SingletonComponent::class)
class TrackingModule {

    @Provides
    fun providesPositionManager(@ApplicationContext context: Context): PositionManager =
        PositionManager(context, Looper.getMainLooper())

    @Provides
    fun providesPositionStackInterceptor(resources: Resources): PositionStackInterceptor =
        PositionStackInterceptor(resources)

    @Provides
    @PositionStackOkHttp
    fun providesOkHttpClient(
        cache: Cache,
        positionStackInterceptor: PositionStackInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor(positionStackInterceptor)
        .cache(cache)
        .build()

    @Provides
    @BigDataCloudServer
    fun providesBigDataCloudRetrofit(
        gson: Gson,
        @AppOkHttp okHttpClient: OkHttpClient,
        res: Resources
    ): Retrofit = Retrofit.Builder()
        .baseUrl(res.getString(R.string.server_bigcloud_url))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()

    @Provides
    @PositionStackServer
    fun providesPositionStackRetrofit(
        gson: Gson,
        @PositionStackOkHttp okHttpClient: OkHttpClient,
        res: Resources
    ): Retrofit = Retrofit.Builder()
        .baseUrl(res.getString(R.string.server_position_stack_url))
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()

    @Provides
    fun providesBigDataCloudService(@BigDataCloudServer retrofit: Retrofit): BigDataCloudService =
        retrofit.create(BigDataCloudService::class.java)

    @Provides
    fun providesPositionStackService(@PositionStackServer retrofit: Retrofit): PositionStackService =
        retrofit.create(PositionStackService::class.java)

    @Provides
    fun providesTrackingRepository(trackingRepositoryImpl: TrackingRepositoryImpl): TrackingRepository =
        trackingRepositoryImpl

    @Provides
    fun providesTrackingDataSource(trackingDataSourceImpl: TrackingDataSourceImpl): TrackingDataSource =
        trackingDataSourceImpl
}
