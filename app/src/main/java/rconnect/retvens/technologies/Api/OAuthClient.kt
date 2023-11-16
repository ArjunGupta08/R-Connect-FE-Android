package rconnect.retvens.technologies.Api

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import rconnect.retvens.technologies.utils.UserSessionManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class OAuthClient<T>(context: Context) {


    private val language = ""
    private val headers = mapOf(
        "language" to language
    )

//        private val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vYmxtaW5kb3JlLmluL2FwaS9sb2dpbiIsImlhdCI6MTY3OTg3MzY4NSwiZXhwIjoxNjc5ODc3Mjg1LCJuYmYiOjE2Nzk4NzM2ODUsImp0aSI6Ikx4MEV0cGljM3ZsV3hnOHEiLCJzdWIiOiIyIiwicHJ2IjoiMjNiZDVjODk0OWY2MDBhZGIzOWU3MDFjNDAwODcyZGI3YTU5NzZmNyJ9.L1Sdk1TDQLTu5yWW6YiVJhfxVfh25qdMLbF50GW5sdQ"

    val token = UserSessionManager(context).getToken()


    private val client =  OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60,TimeUnit.SECONDS)
        .addInterceptor(OAuthInterceptor("$token", headers))
        .build()

    val gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun create(service: Class<T>): T {
        return retrofit.create(service)
    }
}