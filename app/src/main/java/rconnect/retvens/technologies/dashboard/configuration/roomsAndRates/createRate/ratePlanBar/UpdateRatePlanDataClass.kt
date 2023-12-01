package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar

import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.InclusionPlan

data class InclusionUpdatedPlan(
    val inclusionId: String,
    val inclusionType: String,
    val inclusionName: String,
    var postingRule: String,
    var chargeRule: String,
    var rate: String
)

data class UpdateRatePlanDataClass(
    var userId: String,
    var propertyId: String,
    var rateType: String,
    var roomTypeId: String,
    var ratePlanName: String,
    var shortCode: String,
    var inclusionPlan: ArrayList<InclusionPlan>,
    var roomBaseRate: String,
    var mealCharge: String,
    var inclusionCharge: String,
    var roundUp: String,
    var extraAdultRate: String,
    var extraChildRate: String,
    var ratePlanTotal: String
)

data class UpdateCompanyRatePlanDataClass(
    var userId: String,
    var propertyId: String,
    var rateType: String,
    var roomTypeId: String,
    var ratePlanName: String,
    var shortCode: String,
    var ratePlanInclusion: ArrayList<InclusionPlan>,
    var roomBaseRate: String,
    var mealCharge: String,
    var inclusionCharge: String,
    var roundUp: String,
    var extraAdultRate: String,
    var extraChildRate: String,
    var ratePlanTotal: String
)

