import android.content.Context
import com.example.airpollutionmonitor.BuildConfig
import com.example.airpollutionmonitor.data.network.AirMonitorApi
import com.example.airpollutionmonitor.data.network.CacheInterceptor
import com.example.airpollutionmonitor.data.repository.AirPollutionRepository
import com.example.airpollutionmonitor.ui.AirMonitorViewModel
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

const val BASE_URL = "https://data.epa.gov.tw"

val singleModule = module {
    single { headerInterceptor() }
    single { provideCache(androidApplication()) }
    single { okhttpClient(androidApplication(), get(),get()) }
    single { retrofit(get()) }
    single { provideApiService(get()) }
    single { AirPollutionRepository(get()) }
}

fun provideApiService(
    retrofit: Retrofit
) : AirMonitorApi =
    retrofit.create(AirMonitorApi::class.java)

fun retrofit(
    okHttpClient: OkHttpClient
) : Retrofit =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

fun okhttpClient(
    context: Context,
    cache: Cache,
    headerInterceptor: Interceptor
) : OkHttpClient {
    val cacheInterceptor = CacheInterceptor(context)
    return OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
        .addNetworkInterceptor(cacheInterceptor)
        .cache(cache)
        .build()
}
fun headerInterceptor() : Interceptor =
    Interceptor { chain ->
        val request = chain.request()
        val newUrl = request.url.newBuilder()
            .addQueryParameter("api_key", BuildConfig.API_KEY)
            .build()

        val newRequest = request.newBuilder()
            .url(newUrl)
            .build()
        chain.proceed(newRequest)
    }

fun provideCache(context: Context): Cache {
    val cacheSize = 10 * 1024 * 1024
    val cacheFile = File(context.cacheDir, "okhttpCache")
    return Cache(cacheFile, cacheSize.toLong())
}

val viewModelModule = module {
    viewModel { AirMonitorViewModel(get()) }
}