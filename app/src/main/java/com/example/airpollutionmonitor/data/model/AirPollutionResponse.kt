package com.example.airpollutionmonitor.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class AirPollutionResponse(
    @SerializedName("total")
    val total: String = "",
    @SerializedName("offset")
    val offset: String = "",
    @SerializedName("records")
    val records: List<RecordsItem>?,
    @SerializedName("resource_format")
    val resourceFormat: String = "",
    @SerializedName("limit")
    val limit: String = "",
    @SerializedName("resource_id")
    val resourceId: String = "",
    @SerializedName("include_total")
    val includeTotal: Boolean = false
)

data class RecordsItem(
    @SerializedName("sitename")
    val sitename: String = "",
    @SerializedName("county")
    val county: String = "",
    @SerializedName("aqi")
    val aqi: String = "",
    val pollutant: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("so2")
    val so2: String = "",
    @SerializedName("co")
    val co: String = "",
    @SerializedName("o3")
    val o3: String = "",
    @SerializedName("o3_8hr")
    val o3_8Hr: String = "",
    @SerializedName("pm10")
    val pm10: String = "",
    @SerializedName("pm2.5")
    val pm2_5: String = "",
    @SerializedName("no2")
    val no2: String = "",
    @SerializedName("nox")
    val nox: String = "",
    @SerializedName("no")
    val no: String = "",
    @SerializedName("wind_speed")
    val windSpeed: String = "",
    @SerializedName("wind_direc")
    val windDirec: String = "",
    @SerializedName("publishtime")
    val publishTime: String = "",
    @SerializedName("co_8hr")
    val co_8Hr: String = "",
    @SerializedName("pm2.5_avg")
    val pm2_5_avg: String = "",
    @SerializedName("pm10_avg")
    val pm10_avg: String = "",
    @SerializedName("so2_avg")
    val so2_avg: String = "",
    @SerializedName("longitude")
    val longitude: String = "",
    @SerializedName("latitude")
    val latitude: String = "",
    @SerializedName("siteid")
    val siteId: String = "",
)


@Keep
@Parcelize
class AirPolluteList : ArrayList<RecordsItem>(), Parcelable

