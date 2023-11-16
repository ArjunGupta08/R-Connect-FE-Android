package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlaneDiscount

data class RatePlanDiscountData(
    val discountName : String,
    val shortCode : String,
    val discountType : String,
    val discountPercent : String,
    val blackOutDates : ArrayList<String>,
    val applicableOnData : ArrayList<ApplicableOnData>,
    val discountPrice : String,
    val validityPeriodFrom : String,
    val validityPeriodTo : String,
    val deviceType : String
)

data class ApplicableOnData(
    val roomTypeId : String,
    val ratePlans : ArrayList<RatePlansData>,
)

data class RatePlansData(
    val ratePlanId : String,
    val newRatePlanPrice : String
)