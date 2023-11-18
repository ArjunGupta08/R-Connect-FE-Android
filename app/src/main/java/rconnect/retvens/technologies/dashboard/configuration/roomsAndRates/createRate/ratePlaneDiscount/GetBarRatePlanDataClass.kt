package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlaneDiscount

data class BarRatePlan(
    val propertyId: String,
    val barRatePlanId: String,
    val createdBy: String,
    val rateType: String,
    val roomTypeId: String,
    val mealPlanId: String,
    val ratePlanName: String,
    val shortCode: String,
    val inclusion: List<Inclusion>,
    val barRates: String,
    val mealCharge: String,
    val inclusionCharge: String,
    val roundUp: String,
    val extraAdultRate: String,
    val extraChildRate: String,
    val ratePlanTotal: String
)

data class Inclusion(
    val inclusionId: String,
    val inclusionName: String,
    val inclusionType: String,
    val postingRule: String,
    val chargeRule: String,
    val rate: String,
    val _id: String
)

data class GetBarRatePlanDataClass(
    val data: List<BarRatePlan>,
    val statuscode: Int
)

