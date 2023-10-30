package rconnect.retvens.technologies.Api

import rconnect.retvens.technologies.LoginRequest
import rconnect.retvens.technologies.LoginResponse
import rconnect.retvens.technologies.onboarding.authentication.SignUpDataClass
import rconnect.retvens.technologies.onboarding.authentication.SignUpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationInterface {
    @POST("userLogin")
    fun login(@Body loginRequest: LoginRequest):Call<LoginResponse>

    @POST("addUser")
    fun signUp(
        @Body signUpDataClass: SignUpDataClass
    ):Call<SignUpResponse>





}

