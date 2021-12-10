package com.markoid.parky.position.data.services

import com.markoid.parky.position.data.entities.bigdatacloud.BigDataCloudResponse
import com.markoid.parky.position.presentation.utils.PositionConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BigDataCloudService {

    @GET(PositionConstants.BIG_DATA_REVERSE_GEOCODING)
    suspend fun reverseGeocoding(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("localityLanguage") language: String = "es"
    ): Response<BigDataCloudResponse>
}
