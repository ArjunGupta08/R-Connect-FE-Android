package rconnect.retvens.technologies.Api

import rconnect.retvens.technologies.dashboard.channelManager.RatesAndInventory.GetOtaSourceData
import rconnect.retvens.technologies.dashboard.channelManager.RatesAndInventory.GetRatePlansData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.GetPropertyTypeData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.GetPropertyTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.GetPropertyTypeRatingData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.GetBedTypeData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity.GetAmenityType
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.GetRoomType
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.GetRoomTypeData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetChargeRuleArray
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetPostingRuleArray
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetPostingRuleData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

interface DropDownApis {

    @GET("getAmenityType")
    fun getAmenityType (): Call<GetAmenityType>

    @GET("getPropertyType")
    fun getPropertyType(): Call<GetPropertyTypeData>

    @GET("getBedType")
    fun getBedType(): Call<GetBedTypeData>

    @GET("getRating")
    fun getPropertyTypeRating(): Call<GetPropertyTypeRatingData>

    @GET("postingRulesModels")
    fun getPostingRulesModels(): Call<GetPostingRuleArray>

    @GET("getchargeRule")
    fun getChargeRulesModels(): Call<GetChargeRuleArray>

    @GET("getRoomList")
    fun getRoomList(
        @Query("propertyId") propertyId :String,
        @Query("userId") userId :String
    ):Call<GetRoomTypeData>

    @GET("fetchSource")
    fun getOtaList(
        @Query("userId") userId:String,
        @Query("propertyId") propertyId:String
    ):Call<GetOtaSourceData>

    @GET("getRatePlansList")
    fun getRatePlans(
        @Query("userId") userId: String,
        @Query("roomTypeId") roomTypeId:String
    ):Call<GetRatePlansData>


}