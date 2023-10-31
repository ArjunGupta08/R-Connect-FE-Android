package rconnect.retvens.technologies.Api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import rconnect.retvens.technologies.Api.configurationApi.ChainConfiguration
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val baseUrl = "http://159.65.216.104/api/"
object RetrofitObject {
    private val gson = GsonBuilder().setLenient().create()

    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(baseUrl)
        .build()

    val authentication: AuthenticationInterface = retrofit.create(AuthenticationInterface::class.java)
    val chainConfiguration: ChainConfiguration = retrofit.create(ChainConfiguration::class.java)
}