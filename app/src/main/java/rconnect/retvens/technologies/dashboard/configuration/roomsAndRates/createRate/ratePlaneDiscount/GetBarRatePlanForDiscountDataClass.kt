package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlaneDiscount



data class DisCountBarRatePlan(
    val ratePlanName: String,
    val barRatePlanId: String,
    val ratePlanTotal: String,
    val inclusion: List<RatePlanInclusion>,
    val extraAdultRate: String,
    val extraChildRate: String
)

data class RoomData(
    val propertyId: String,
    val roomTypeId: String,
    val roomTypeName: String,
    val barRatePlans: List<DisCountBarRatePlan>
)

data class GetBarRatePlanForDiscountDataClass(
    val data: List<RoomData>,
    val statuscode: Int
)