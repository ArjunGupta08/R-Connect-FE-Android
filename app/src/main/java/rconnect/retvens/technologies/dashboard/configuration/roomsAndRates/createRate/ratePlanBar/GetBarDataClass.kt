package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar

import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.InclusionPlan

data class Inclusion(
    val inclusionId: String,
    val inclusionName: String,
    val inclusionType: String,
    val postingRule: String,
    val chargeRule: String,
    val rate: String,
    val _id: String
)

data class BarRates(
    val roomBaseRate: String,
    val mealCharge: String,
    val inclusionCharge: String,
    val roundUp: String,
    val extraAdultRate: String,
    val extraChildRate: String,
    val ratePlanTotal: String
)

data class Data(
    val propertyId: String,
    val barRatePlanId: String,
    val ratePlanName: String,
    val shortCode: String,
    val inclusion: ArrayList<InclusionPlan>,
    val barRates: BarRates,
    val roomTypeId: String,
    val mealPlanId: String
)

data class GetBarDataClass(
    val data: Data,
    val statuscode: Int
)


data class BarData(
    val propertyId: String,
    val roomTypeId: String,
    val barRatePlanId: String,
    val ratePlanTotal: String,
    val inclusion: ArrayList<InclusionPlan>,
    val extraChildRate: String,
    val extraAdultRate: String,
    val ratePlanName: String,
)

data class GetBarRateDataClass(
    val `data`: ArrayList<BarData>,
    val statuscode: Int
)

