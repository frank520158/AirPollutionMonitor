package com.example.airpollutionmonitor.data.network

import com.example.airpollutionmonitor.data.model.AirPollutionResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AirMonitorApi {

    @GET("api/v2/aqx_p_432?sort=ImportDate%20desc&format=json")
    suspend fun getAirPollution(
        @Query("limit") limit: Int,
    ): AirPollutionResponse

}