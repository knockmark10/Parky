package com.markoid.parky.position.data.services

import com.markoid.parky.position.data.entities.positionstack.PositionStackResponse
import com.markoid.parky.position.presentation.utils.PositionConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PositionStackService {

    @GET(PositionConstants.POSITION_STACK_FORWARD_GEOCODING)
    suspend fun forwardGeocoding(
        @Query("query") query: String,
    ): Response<PositionStackResponse>

    @GET(PositionConstants.POSITION_STACK_REVERSE_GEOCODING)
    suspend fun reverseGeocoding(
        @Query("query") query: String
    ): Response<PositionStackResponse>
}
