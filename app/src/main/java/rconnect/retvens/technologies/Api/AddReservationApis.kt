package rconnect.retvens.technologies.Api

import rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment.BookingSorceDataClass
import rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment.RatePlanDataClass
import rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment.RateTypeDataClass
import rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment.ReservationTypeDataClass
import rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment.RoomTypeDataClass
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AddReservationApis {


    @GET("getRateTypeName")
    fun getRateType():Call<RateTypeDataClass>

    @GET("getBookingSource")
    fun getBookingSource(
        @Query("userId") userId:String,
        @Query("propertyId") propertyId:String
    ):Call<BookingSorceDataClass>

    @GET("getReservation")
    fun getReservationType(
        @Query("userId") userId:String,
        @Query("propertyId") propertyId:String
    ):Call<ReservationTypeDataClass>


    @GET("getRoomList")
    fun getRoomType(
        @Query("userId") userId:String,
        @Query("propertyId") propertyId:String
    ):Call<RoomTypeDataClass>

    @GET("getRatePlansList")
    fun getRatePlan(
        @Query("roomTypeId") roomTypeId:String,
        @Query("userId") userId:String
    ):Call<RatePlanDataClass>


}