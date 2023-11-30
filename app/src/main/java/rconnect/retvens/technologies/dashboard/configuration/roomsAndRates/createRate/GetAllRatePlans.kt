package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate
data class RatePlan(
    val rateType: String,
    val shortCode: String,
    val ratePlanName: String,
    val roomTypeName: String,
    val inclusion: Int,
    val extraAdultRate: String,
    val extraChildRate: String,
    val ratePlanTotal: String
)

data class GetAllRatePlans(
    val companyRatePlan: List<RatePlan>,
    val barRatePlan: List<RatePlan>,
    val packageRatePlan: List<RatePlan>,
    val discountplans: List<RatePlan>,
    val statuscode: Int
)
