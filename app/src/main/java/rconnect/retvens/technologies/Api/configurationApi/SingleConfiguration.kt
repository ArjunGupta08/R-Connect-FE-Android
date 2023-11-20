package rconnect.retvens.technologies.Api.configurationApi


import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import rconnect.retvens.technologies.dashboard.configuration.properties.FetchPropertyData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.CreateRoomData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.UpdateRoomData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.AddCompanyRatePlanDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanPackage.AddPackageDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlaneDiscount.AddDiscountDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlaneDiscount.GetBarRatePlanForDiscountDataClass
import rconnect.retvens.technologies.onboarding.ResponseData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface SingleConfiguration {

    /*---------------------------------------Add Room-----------------------------------------*/
    @POST("createRoom")
    fun createRoomApi(
        @Body createRoomData: CreateRoomData
    ): Call<ResponseData>

    /*---------------------------------------Update Room-----------------------------------------*/
    @PATCH("updateRoom/{roomTypeId}")
    fun updateRoomApi(
        @Path("roomTypeId") roomTypeId : String,
        @Body updateRoomData: UpdateRoomData
    ): Call<ResponseData>

    /*---------------------------------------Add Rate Plan Package-----------------------------------------*/
    @POST("addPackage")
    fun addPackage (
        @Body addPackageDataClass: AddPackageDataClass
    ): Call<ResponseData>
    @POST("addCompanyRatePlan")
    fun addCompanyRatePlanApi (
        @Body addCompanyRatePlanDataClass: AddCompanyRatePlanDataClass
    ): Call<ResponseData>

    @POST("barRatePlan")
    fun barRatePlanApi (
        @Body addCompanyRatePlanDataClass: AddCompanyRatePlanDataClass
    ): Call<ResponseData>


    @GET("getRoomsWithPlans")
    fun getRoomWithPlans(
        @Query("userId")userId:String,
        @Query("propertyId")propertyId:String
    ):Call<GetBarRatePlanForDiscountDataClass>

    @POST("addDiscountPlan")
    fun addDiscount(
        @Query("userId")userId:String,
        @Body addDiscountDataClass: AddDiscountDataClass
    ):Call<ResponseData>
}