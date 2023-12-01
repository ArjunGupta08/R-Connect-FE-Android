package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany



data class RatePlanData(
    val rateType: String,
    val roomTypeId: String,
    val mealPlanId: String,
    val companyName: String,
    val ratePlanInclusion: ArrayList<InclusionPlan>,
    val ratePlanName: String,
    val shortCode: String,
    val inclusionTotal: String,
    val roomBaseRate: String,
    val mealCharge: String,
    val inclusionCharge: String,
    val roundUp: String,
    val extraAdultRate: String,
    val extraChildRate: String,
    val ratePlanTotal: String
)

data class GetCompanyDataClass(
    val data: RatePlanData,
    val statuscode: Int
)

