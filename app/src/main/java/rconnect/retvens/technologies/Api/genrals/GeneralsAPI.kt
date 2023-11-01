package rconnect.retvens.technologies.Api.genrals


import rconnect.retvens.technologies.dashboard.configuration.reservation.AddReservationData
import rconnect.retvens.technologies.onboarding.ResponseData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface GeneralsAPI {

    /*---------------------------------------Create Reservation-----------------------------------------*/
    @POST("addReservationType ")
    fun createReservationApi(
        @Body addReservationData: AddReservationData
    ): Call<ResponseData>

}