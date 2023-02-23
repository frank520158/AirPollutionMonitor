import com.example.airpollutionmonitor.BuildConfig
import com.example.airpollutionmonitor.data.network.AirMonitorApi
import com.example.airpollutionmonitor.data.repository.AirPollutionRepository
import com.example.airpollutionmonitor.ui.AirMonitorViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

const val BASE_URL = "https://data.epa.gov.tw"

val singleModule = module {
    single { headerInterceptor() }
    single { okhttpClient(get()) }
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
    headerInterceptor: Interceptor
) : OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
        .build()

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




/*val repoModule = module {
    factory { AirPollutionRepository(get()) }
}*/

val viewModelModule = module {
    viewModel { AirMonitorViewModel(get()) }
}