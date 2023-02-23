package com.example.airpollutionmonitor.data.repository

import com.example.airpollutionmonitor.data.model.AirPollutionResponse
import com.example.airpollutionmonitor.data.network.AirMonitorApi
import com.example.airpollutionmonitor.util.ApiResultWrapper
import com.example.airpollutionmonitor.util.safeApiCall
import kotlinx.coroutines.withContext

class AirPollutionRepository(
    private val api: AirMonitorApi,
) {

    suspend fun getAirMonitorData(): ApiResultWrapper<AirPollutionResponse> =
            safeApiCall { api.getAirPollution(1000) }
}