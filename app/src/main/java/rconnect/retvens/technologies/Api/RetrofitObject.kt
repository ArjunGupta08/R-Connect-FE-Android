package rconnect.retvens.technologies.Api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObject {
    val gson = GsonBuilder().setLenient().create()

    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl("https://api.hotelratna.com/api/")
        .build()
        .create(AuthenticationInterface::class.java)
}