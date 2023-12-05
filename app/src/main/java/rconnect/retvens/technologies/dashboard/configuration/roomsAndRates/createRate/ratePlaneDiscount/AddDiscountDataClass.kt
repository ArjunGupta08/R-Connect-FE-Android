package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlaneDiscount

data class RatePlanInclusion(
    val inclusionId: String,
    val inclusionName: String,
    val postingRule: String,
    val chargeRule: String,
    val rate: String
)

data class AddDiscountDataClass(
    val rateType: String,
    val propertyId: String,
    val roomTypeId: String,
    val ratePlanId: String,
    val discountName: String,
    val shortCode: String,
    val discountType: String,
    val discountPercent: String,
    val discountPrice: String,
    val validityPeriodFrom: String,
    val validityPeriodTo: String,
    val blackOutDates: List<String>,
    val ratePlanInclusion: List<RatePlanInclusion>,
    val discountTotal: String,
    val extraAdultRate: String,
    val extraChildRate: String
)

