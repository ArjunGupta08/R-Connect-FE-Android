package rconnect.retvens.technologies.Api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import rconnect.retvens.technologies.Api.configurationApi.ChainConfiguration
import rconnect.retvens.technologies.Api.configurationApi.SingleConfiguration
import rconnect.retvens.technologies.Api.genrals.GeneralsAPI
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.GetRoomType
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

const val baseUrl = "https://api.hotelratna.com/api/"
object RetrofitObject {
    private val gson = GsonBuilder().setLenient().create()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(baseUrl)
        .build()

    val authentication: AuthenticationInterface = retrofit.create(AuthenticationInterface::class.java)
    val chainConfiguration: ChainConfiguration = retrofit.create(ChainConfiguration::class.java)
    val singleConfiguration: SingleConfiguration = retrofit.create(SingleConfiguration::class.java)
    val inventoryInterface:RatesAndInventoryInterface = retrofit.create(RatesAndInventoryInterface::class.java)
    val dropDownApis: DropDownApis = retrofit.create(DropDownApis::class.java)
    val getGeneralsAPI: GeneralsAPI = retrofit.create(GeneralsAPI::class.java)
}