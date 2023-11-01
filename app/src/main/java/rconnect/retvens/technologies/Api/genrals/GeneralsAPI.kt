package rconnect.retvens.technologies.Api.genrals


import rconnect.retvens.technologies.dashboard.configuration.reservation.CreateReservationTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.reservation.GetReservationTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.reservation.UpdateReservationTypeDataClass
import rconnect.retvens.technologies.onboarding.ResponseData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface GeneralsAPI {

    /*---------------------------------------Reservation-----------------------------------------*/
    @POST("addReservationType ")
    fun createReservationApi(
        @Body addReservationData: CreateReservationTypeDataClass
    ): Call<ResponseData>
    @GET("getReservation")
    fun getReservationApi (
        @Query("userId") userId : String,
        @Query("propertyId") propertyId : String,
    ): Call<GetReservationTypeDataClass>
    @PATCH("updateReservationType")
    fun updateReservationTypeApi (
        @Query("userId") userId : String,
        @Query("reservationTypeId") reservationTypeId : String,
        @Body updateReservationTypeDataClass: UpdateReservationTypeDataClass
    ): Call<GetReservationTypeDataClass>

}