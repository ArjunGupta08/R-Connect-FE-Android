package rconnect.retvens.technologies.Api

import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.GetPropertyTypeData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.GetPropertyTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addPropertyFrags.GetPropertyTypeRatingData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.GetRoomType
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.GetRoomTypeData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

interface DropDownApis {

    @GET("getPropertyType")
    fun getPropertyType(): Call<GetPropertyTypeData>

    @GET("getRating")
    fun getPropertyTypeRating(): Call<GetPropertyTypeRatingData>

    @GET("getRoomList")
    fun getRoomList(
        @Query("propertyId") propertyId :String,
        @Query("userId") userId :String
    ):Call<GetRoomTypeData>
}