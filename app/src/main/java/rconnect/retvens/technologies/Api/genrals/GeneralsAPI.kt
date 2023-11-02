package rconnect.retvens.technologies.Api.genrals


import rconnect.retvens.technologies.dashboard.configuration.billings.AddPaymentTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.billings.GetPaymentTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.billings.UpdatePaymentTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.guestsAndReservation.identityType.AddIdentityTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.guestsAndReservation.reservationType.CreateReservationTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.guestsAndReservation.identityType.GetIdentityTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.guestsAndReservation.reservationType.GetReservationTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.guestsAndReservation.identityType.UpdateIdentityTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.guestsAndReservation.reservationType.UpdateReservationTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.others.transportationTypes.AddTransportationTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.others.transportationTypes.GetTransportationTypeData
import rconnect.retvens.technologies.dashboard.configuration.others.transportationTypes.GetTransportationTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.others.transportationTypes.UpdateTransportationTypeDataClass
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

    /*--------------------------------------- Payment Type-----------------------------------------*/
    @POST("addPaymentType")
    fun addPaymentTypeApi(
        @Query("userId") userId: String,
        @Body addPaymentTypeDataClass: AddPaymentTypeDataClass
    ): Call<ResponseData>
    @GET("getPaymentTypes")
    fun getPaymentTypeApi (
        @Query("userId") userId : String,
        @Query("propertyId") propertyId : String,
    ): Call<GetPaymentTypeDataClass>
    @PATCH("patchPaymentType")
    fun updatePaymentTypeApi (
        @Query("userId") userId : String,
        @Query("paymentTypeId") paymentTypeId : String,
        @Body updatePaymentTypeDataClass: UpdatePaymentTypeDataClass
    ): Call<GetReservationTypeDataClass>

    /*--------------------------------------- Payment Type-----------------------------------------*/
    @POST("postIdentity")
    fun addIdentityTypeApi(
        @Body addIdentityTypeDataClass: AddIdentityTypeDataClass
    ): Call<ResponseData>
    @GET("fetchIdentity")
    fun getIdentityTypeApi (
        @Query("userId") userId : String,
        @Query("propertyId") propertyId : String,
    ): Call<GetIdentityTypeDataClass>
    @PATCH("patchIdentityType")
    fun updateIdentityTypeApi (
        @Query("userId") userId : String,
        @Query("identityTypeId") identityTypeId : String,
        @Body updateIdentityTypeDataClass: UpdateIdentityTypeDataClass
    ): Call<GetReservationTypeDataClass>

    /*--------------------------------------- Transportation Mode Type-----------------------------------------*/
    @POST("addTransportation")
    fun addTransportationModeTypeApi(
        @Body addTransportationTypeDataClass: AddTransportationTypeDataClass
    ): Call<ResponseData>
    @GET("getTransportation ")
    fun getTransportationModeTypeApi (
        @Query("userId") userId : String,
        @Query("propertyId") propertyId : String,
    ): Call<GetTransportationTypeDataClass>
    @PATCH("updateTransportation")
    fun updateTransportationModeTypeApi (
        @Query("userId") userId : String,
        @Query("transportationId") transportationId : String,
        @Body updateTransportationTypeDataClass: UpdateTransportationTypeDataClass
    ): Call<GetReservationTypeDataClass>

}