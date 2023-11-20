package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlaneDiscount

data class RatePlan(
    val rateplanId: String,
    val newRatePlanPrice: String
)


data class ApplicableOn(
    val roomTypeId: String,
    val ratePlans: List<RatePlan>
)

data class AddDiscountDataClass(
    val deviceType: String,
    val propertyId: String,
    val discountName: String,
    val shortCode: String,
    val discountType: String,
    val discountPercent: String,
    val discountPrice: String,
    val validityPeriodFrom: String,
    val validityPeriodTo: String,
    val blackOutDates: List<String>,
    val applicableOn: List<ApplicableOn>
)
