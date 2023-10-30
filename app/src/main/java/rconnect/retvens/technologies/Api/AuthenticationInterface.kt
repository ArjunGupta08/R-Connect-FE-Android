package rconnect.retvens.technologies.Api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import rconnect.retvens.technologies.LoginRequest
import rconnect.retvens.technologies.LoginResponse
import rconnect.retvens.technologies.onboarding.authentication.SignUpDataClass
import rconnect.retvens.technologies.onboarding.authentication.SignUpResponse
import rconnect.retvens.technologies.onboarding.ResponseData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthenticationInterface {
    @POST("userLogin")
    fun login(@Body loginRequest: LoginRequest):Call<LoginResponse>

    @POST("addUser")
    fun signUp(
        @Body signUpDataClass: SignUpDataClass
    ):Call<SignUpResponse>

    @Multipart
    @PATCH("userEdit")
    fun userEdit(
        @Part("userId") userId: RequestBody,
        @Part("propertyTypeSOC") propertyTypeSOC: RequestBody,
        @Part("websiteUrl") websiteUrl: RequestBody,
        @Part hotelLogo: MultipartBody.Part,
        @Part("numberOfProperties") numberOfProperties: RequestBody,
        @Part("propertyType") propertyType: RequestBody,
        @Part("baseCurrency") baseCurrency: RequestBody,
        @Part("propertyAddress1") propertyAddress1: RequestBody,
        @Part("propertyName") propertyName: RequestBody,
        @Part("propertyTypeName") propertyTypeName: RequestBody,
        @Part("postCode") postCode: RequestBody,
        @Part("state") state: RequestBody,
        @Part("city") city: RequestBody,
        @Part("country") country: RequestBody,
        @Part("starCategory") starCategory: RequestBody,
        @Part("ratePercent") ratePercent: RequestBody,
        @Part("roomsInProperty") roomsInProperty: RequestBody,
        @Part("taxName") taxName: RequestBody,
        @Part("registrationNumber") registrationNumber: RequestBody,
    ):Call<ResponseData>

    @Multipart
    @PATCH("userEdit")
    fun firstOnboarding(
        @Part("userId") userId: RequestBody,
        @Part("propertyTypeSOC") propertyTypeSOC: RequestBody,
        @Part("websiteUrl") websiteUrl: RequestBody,
        @Part hotelLogo: MultipartBody.Part,
        @Part("propertyType") propertyType: RequestBody,
        @Part("propertyAddress1") propertyAddress1: RequestBody,
        @Part("propertyName") propertyName: RequestBody,
        @Part("postCode") postCode: RequestBody,
        @Part("state") state: RequestBody,
        @Part("city") city: RequestBody,
        @Part("country") country: RequestBody
    ):Call<ResponseData>

    @Multipart
    @PATCH("userEdit")
    fun firstOnboardingWithoutImage(
        @Part("userId") userId: RequestBody,
        @Part("propertyTypeSOC") propertyTypeSOC: RequestBody,
        @Part("websiteUrl") websiteUrl: RequestBody,
        @Part("propertyType") propertyType: RequestBody,
        @Part("propertyAddress1") propertyAddress1: RequestBody,
        @Part("propertyName") propertyName: RequestBody,
        @Part("postCode") postCode: RequestBody,
        @Part("state") state: RequestBody,
        @Part("city") city: RequestBody,
        @Part("country") country: RequestBody
    ):Call<ResponseData>

    @Multipart
    @PATCH("userEdit")
    fun secondOnboarding(
        @Part("userId") userId: RequestBody,
        @Part("starCategory") starCategory: RequestBody,
        @Part("roomsInProperty") roomsInProperty: RequestBody,
        @Part("taxName") taxName: RequestBody,
        @Part("registrationNumber") registrationNumber: RequestBody,
        @Part("ratePercent") ratePercent: RequestBody,
    ):Call<ResponseData>

    @Multipart
    @PATCH("userEdit")
    fun firstChainOnboarding(
        @Part("userId") userId: RequestBody,
        @Part("propertyTypeSOC") propertyTypeSOC: RequestBody,
        @Part("websiteUrl") websiteUrl: RequestBody,
        @Part hotelLogo: MultipartBody.Part,
        @Part("numberOfProperties") numberOfProperties: RequestBody,
        @Part("propertyType") propertyType: RequestBody,
        @Part("baseCurrency") baseCurrency: RequestBody,
        @Part("propertyName") propertyName: RequestBody,
    ):Call<ResponseData>

    @Multipart
    @PATCH("userEdit")
    fun firstChainOnboardingWithoutImg(
        @Part("userId") userId: RequestBody,
        @Part("propertyTypeSOC") propertyTypeSOC: RequestBody,
        @Part("websiteUrl") websiteUrl: RequestBody,
        @Part("numberOfProperties") numberOfProperties: RequestBody,
        @Part("propertyType") propertyType: RequestBody,
        @Part("baseCurrency") baseCurrency: RequestBody,
        @Part("propertyName") propertyName: RequestBody,
    ):Call<ResponseData>




}

