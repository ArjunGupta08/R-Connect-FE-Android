package rconnect.retvens.technologies.Api.configurationApi


import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import rconnect.retvens.technologies.dashboard.configuration.properties.FetchPropertyData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.AddPropertyResponseDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.GetPropertyData
import rconnect.retvens.technologies.onboarding.ResponseData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
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
    ): Call<AddPropertyResponseDataClass>

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
    ): Call<AddPropertyResponseDataClass>

    /*---------------------------------------Edit Property-----------------------------------------*/
    @Multipart
    @PATCH("editProperty")
    fun editPropertyApi(
        @Query("userId") userIdQuery: String,
        @Query("propertyId") propertyId: String,
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
    ): Call<AddPropertyResponseDataClass>

    @Multipart
    @PATCH("editProperty")
    fun editPropertyWithoutLogoApi(
        @Query("userId") userIdQuery: String,
        @Query("propertyId") propertyId: String,
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
    ): Call<AddPropertyResponseDataClass>

    @GET("fetchProperty")
    fun fetchProperty(
        @Query("userId") userId: String
    ): Call<FetchPropertyData>

    @GET("getPropertyById")
    fun getPropertyById(
        @Query("userId") userId: String,
        @Query("propertyId") propertyId: String
    ): Call<GetPropertyData>

    @Multipart
    @PATCH("uploadRoomImage")
    fun uploadRoomsImages(
        @Query("roomTypeId") roomTypeId: String,
        @Query("userId") userId: String,
        @Part image: MultipartBody.Part,
        @Part("image[0]rooms") rooms: RequestBody
    ): Call<ResponseData>

    @Multipart
    @PATCH("uploadPropertyImages")
    fun uploadPropertyImages(
        @Query("userId") userId: String,
        @Query("propertyId") propertyId: String,
        @Part("imageTags[0][imageTags]") tag1: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<ResponseData>
}