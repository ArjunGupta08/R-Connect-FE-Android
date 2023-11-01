package rconnect.retvens.technologies.Api.configurationApi


import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import rconnect.retvens.technologies.dashboard.configuration.properties.FetchPropertyData
import rconnect.retvens.technologies.onboarding.ResponseData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ChainConfiguration {

    /*---------------------------------------Add Property-----------------------------------------*/
    @Multipart
    @POST("createProperty")
    fun addPropertyApi(
        @Part hotelLogo: MultipartBody.Part,
        @Part("propertyName") propertyName: RequestBody,
        @Part("propertyType") propertyType: RequestBody,
        @Part("propertyRating") propertyRating: RequestBody,
        @Part("websiteUrl") websiteUrl: RequestBody,
        @Part("propertyDescription") propertyDescription: RequestBody,
        @Part("amenityIds") amenityIds: RequestBody,
        @Part("propertyAddress1") propertyAddress1: RequestBody,
        @Part("propertyAddress2") propertyAddress2: RequestBody,
        @Part("postCode") postCode: RequestBody,
        @Part("city") city: RequestBody,
        @Part("state") state: RequestBody,
        @Part("country") country: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("reservationPhone") reservationPhone: RequestBody,
        @Part("propertyEmail") propertyEmail: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("userId") userId: RequestBody,
    ): Call<ResponseData>

    @Multipart
    @POST("createProperty")
    fun addPropertyWithoutLogoApi(
        @Part("propertyName") propertyName: RequestBody,
        @Part("propertyType") propertyType: RequestBody,
        @Part("propertyRating") propertyRating: RequestBody,
        @Part("websiteUrl") websiteUrl: RequestBody,
        @Part("propertyDescription") propertyDescription: RequestBody,
        @Part("amenityIds") amenityIds: RequestBody,
        @Part("propertyAddress1") propertyAddress1: RequestBody,
        @Part("propertyAddress2") propertyAddress2: RequestBody,
        @Part("postCode") postCode: RequestBody,
        @Part("city") city: RequestBody,
        @Part("state") state: RequestBody,
        @Part("country") country: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("reservationPhone") reservationPhone: RequestBody,
        @Part("propertyEmail") propertyEmail: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("userId") userId: RequestBody,
    ): Call<ResponseData>

    @GET("fetchAmenity")
    fun getAmenities(
        @Query("propertyId") propertyId: String,
        @Query("propertyType") propertyType: String,
        @Query("userId") userId: String
    )

    @GET("fetchProperty")
    fun fetchProperty(
        @Query("userId") userId: String
    ): Call<FetchPropertyData>
}