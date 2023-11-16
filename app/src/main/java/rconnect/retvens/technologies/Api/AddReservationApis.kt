package rconnect.retvens.technologies.Api

import rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment.AvailableRoomDataClass
import rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment.BookingResponse
import rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment.BookingSorceDataClass
import rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment.RatePlanDataClass
import rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment.RateTypeDataClass
import rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment.ReservationDataClass
import rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment.ReservationTypeDataClass
import rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment.RoomTypeDataClass
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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

    @GET("getRoomAvailability")
    fun getAvailableRoom(
        @Query("userId") userId:String,
        @Query("propertyId") propertyId:String,
        @Query("checkInDate") checkInDate:String,
        @Query("checkOutDate") checkOutDate:String,
    ):Call<AvailableRoomDataClass>

    @POST("createBooking")
    fun generateBooking(
        @Body addReserVation:ReservationDataClass
    ):Call<BookingResponse>
}