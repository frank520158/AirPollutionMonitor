package com.example.airpollutionmonitor.data.network

import android.content.Context
import com.example.airpollutionmonitor.util.hasNetworkToDownload
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class CacheInterceptor(val context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response: Response = chain.proceed(request)
        val onlineCacheTime = 60 * 60 * 1 //1 hours

        return if (context.hasNetworkToDownload().not()) {
            response.newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, only-if-cached, max-age=$onlineCacheTime")
                .build()
        } else {
            response.newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, max-age=$onlineCacheTime")
                .build()
        }
    }
}