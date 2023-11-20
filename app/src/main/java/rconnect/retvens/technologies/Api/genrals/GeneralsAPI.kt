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
import rconnect.retvens.technologies.dashboard.configuration.others.businessSource.AddBusinessSourceDataClass
import rconnect.retvens.technologies.dashboard.configuration.others.businessSource.GetBusinessSourceDataClass
import rconnect.retvens.technologies.dashboard.configuration.others.businessSource.UpdateBusinessSourceDataClass
import rconnect.retvens.technologies.dashboard.configuration.others.holiday.AddHolidayDataClass
import rconnect.retvens.technologies.dashboard.configuration.others.holiday.GetHotelDataClass
import rconnect.retvens.technologies.dashboard.configuration.others.holiday.UpdateHolidayDataClass
import rconnect.retvens.technologies.dashboard.configuration.others.seasons.AddSeasonDataClass
import rconnect.retvens.technologies.dashboard.configuration.others.seasons.GetSeasonData
import rconnect.retvens.technologies.dashboard.configuration.others.seasons.GetSeasonDataClass
import rconnect.retvens.technologies.dashboard.configuration.others.transportationTypes.AddTransportationTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.others.transportationTypes.GetTransportationTypeData
import rconnect.retvens.technologies.dashboard.configuration.others.transportationTypes.GetTransportationTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.others.transportationTypes.UpdateTransportationTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity.AmenityDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity.FetchAmenities
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity.GetAmenityIcon
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity.PatchAmenityData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.amenity.PostAmenityData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar.GetMealData
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.AddInclusionsDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.UpdateInclusionDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.mealPlan.GetMealPlanDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.mealPlan.MealPlanDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.mealPlan.UpdateMealPlanData
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
        @Query("targetTimeZone") targetTimeZone : String? = "Asia/Kolkata"
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
        @Query("targetTimeZone") targetTimeZone : String? = "Asia/Kolkata"
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
        @Query("targetTimeZone") targetTimeZone : String? = "Asia/Kolkata"
    ): Call<GetIdentityTypeDataClass>
    @PATCH("patchIdentityType")
    fun updateIdentityTypeApi (
        @Query("userId") userId : String,
        @Query("identityTypeId") identityTypeId : String,
        @Body updateIdentityTypeDataClass: UpdateIdentityTypeDataClass
    ): Call<ResponseData>

    /*--------------------------------------- Transportation Mode Type-----------------------------------------*/
    @POST("addTransportation")
    fun addTransportationModeTypeApi(
        @Body addTransportationTypeDataClass: AddTransportationTypeDataClass
    ): Call<ResponseData>
    @GET("getTransportation ")
    fun getTransportationModeTypeApi (
        @Query("userId") userId : String,
        @Query("propertyId") propertyId : String,
        @Query("targetTimeZone") targetTimeZone : String? = "Asia/Kolkata"
    ): Call<GetTransportationTypeDataClass>
    @PATCH("updateTransportation")
    fun updateTransportationModeTypeApi (
        @Query("userId") userId : String,
        @Query("transportationId") transportationId : String,
        @Body updateTransportationTypeDataClass: UpdateTransportationTypeDataClass
    ): Call<ResponseData>

//    business sources

    @POST("addBusinessSources")
    fun addBusinessSourceApi(
    @Body addBusinessSourceDataClass: AddBusinessSourceDataClass
    ): Call<ResponseData>
    @GET("getBusinessSources ")
    fun getBusinessSourceApi (
        @Query("userId") userId : String,
        @Query("propertyId") propertyId : String,
        @Query("targetTimeZone") targetTimeZone : String? = "Asia/Kolkata"
    ): Call<GetBusinessSourceDataClass>
    @PATCH("updateBusinessSources")
    fun updateBusinessSourceApi (
        @Query("userId") userId : String,
        @Query("sourceId") sourceId : String,
        @Body updateBusinessSourceDataClass: UpdateBusinessSourceDataClass
    ): Call<ResponseData>

    /*--------------------------------------- Booking Source Type-----------------------------------------*/
//    @POST("addTransportation")
//    fun addBookingSourceTypeApi(
//        @Body addTransportationTypeDataClass: AddTransportationTypeDataClass
//    ): Call<ResponseData>

    @GET("getBookingSource ")
    fun getBookingSourceTypeApi (
        @Query("userId") userId : String,
        @Query("propertyId") propertyId : String,
        @Query("propertyId") targetTimeZone: String
    ): Call<GetTransportationTypeDataClass>
//    @PATCH("updateTransportation")
//    fun updateBookingSourceTypeApi (
//        @Query("userId") userId : String,
//        @Query("transportationId") transportationId : String,
//        @Body updateTransportationTypeDataClass: UpdateTransportationTypeDataClass
//    ): Call<GetReservationTypeDataClass>


    /*--------------------------------------- Season Type-----------------------------------------*/
    @POST("postSeason")
    fun addSeasonApi(
        @Body addSeasonDataClass: AddSeasonDataClass
    ): Call<ResponseData>

    @GET("getSeasons")
    fun getSeasonApi (
        @Query("userId") userId : String,
        @Query("propertyId") propertyId : String,
        @Query("targetTimeZone") targetTimeZone : String? = "Asia/Kolkata"
    ): Call<GetSeasonDataClass>
//    @PATCH("updateTransportation")
//    fun updateBookingSourceTypeApi (
//        @Query("userId") userId : String,
//        @Query("transportationId") transportationId : String,
//        @Body updateTransportationTypeDataClass: UpdateTransportationTypeDataClass
//    ): Call<GetReservationTypeDataClass>

    /*--------------------------------------- Holiday-----------------------------------------*/
    @POST("postHoliday")
    fun addHolidayApi(
        @Body addHolidayDataClass: AddHolidayDataClass
    ): Call<ResponseData>

    @GET("getHoliday")
    fun getHolidayApi (
        @Query("userId") userId : String,
        @Query("propertyId") propertyId : String,
        @Query("targetTimeZone") targetTimeZone : String? = "Asia/Kolkata"
    ): Call<GetHotelDataClass>

    @PATCH("patchHoliday")
    fun updateHolidayApi (
        @Query("holidayId") holidayId : String,
        @Query("userId") userId : String,
        @Body updateHolidayDataClass: UpdateHolidayDataClass
    ): Call<ResponseData>

    /*--------------------------------------- Inclusions-----------------------------------------*/
    @POST("postInclusion")
    fun addInclusionsApi(
        @Body addInclusionsDataClass: AddInclusionsDataClass
    ): Call<ResponseData>

    @GET("getInclusion")
    fun getInclusionApi (
        @Query("userId") userId : String,
        @Query("propertyId") propertyId : String,
        @Query("targetTimeZone") targetTimeZone : String? = "Asia/Kolkata"
    ): Call<GetInclusionsDataClass>

    @PATCH("patchInclusion")
    fun updateInclusionsApi (
        @Query("userId") userId : String,
        @Query("inclusionId") inclusionId : String,
        @Body updateInclusionDataClass: UpdateInclusionDataClass
    ): Call<ResponseData>

    /*--------------------------------------- Meal Plan-----------------------------------------*/
    @POST("postMealPlan")
    fun postMealPlanApi(
        @Body mealPlanDataClass: MealPlanDataClass
    ): Call<ResponseData>
    @GET("getMealPlan")
    fun getMealPlanApi (
        @Query("userId") userId : String,
        @Query("propertyId") propertyId : String,
        @Query("targetTimeZone") targetTimeZone : String? = "Asia/Kolkata"
    ): Call<GetMealPlanDataClass>

    @GET("getMealPlan")
    fun getMealPlanApi2 (
        @Query("userId") userId : String,
        @Query("propertyId") propertyId : String,
        @Query("targetTimeZone") targetTimeZone : String? = "Asia/Kolkata"
    ): Call<GetMealData>

    @PATCH("patchMeal")
    fun updateMealPlanApi (
        @Query("userId") userId : String,
        @Query("mealPlanId") mealPlanId : String,
        @Body updateMealPlanData: UpdateMealPlanData
    ): Call<ResponseData>

    /*--------------------------------------- Amenity-----------------------------------------*/
    @GET("getAmenity")
    fun getAmenityApi (
        @Query("propertyId") propertyId : String
    ): Call<AmenityDataClass>

    @GET("getAmenityIcon")
    fun getAmenityIconApi (): Call<GetAmenityIcon>

    @POST("postAmenity")
    fun postAmenityApi(
        @Body postAmenityData: PostAmenityData
    ): Call<ResponseData>

    @GET("getAmenities")
    fun fetchAmenitiesApi(
        @Query("userId") userId : String,
        @Query("propertyId") propertyId : String,
        @Query("targetTimeZone") targetTimeZone : String? = "Asia/Kolkata"
    ): Call<FetchAmenities>

    @PATCH("patchAmenity")
    fun patchAmenityApi(
        @Query("userId") userId : String,
        @Query("amenityId") amenityId : String,
        @Body patchAmenityData: PatchAmenityData
    ): Call<ResponseData>

}