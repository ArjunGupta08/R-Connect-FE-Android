package rconnect.retvens.technologies.Api

import rconnect.retvens.technologies.LoginRequest
import rconnect.retvens.technologies.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {
    @POST("userLogin")
    fun login(@Body loginRequest: LoginRequest):Call<LoginResponse>





}

