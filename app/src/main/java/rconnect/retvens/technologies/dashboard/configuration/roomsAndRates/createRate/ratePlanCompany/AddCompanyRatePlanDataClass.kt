package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany

import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsData

data class AddCompanyRatePlanDataClass(
    var userId : String,
    var propertyId : String,
    var roomTypeId:String,
    var rateType : String,
    var rateTypeId : String,
    val companyId : String,
    var ratePlanName : String,
    var mealPlanId : String,
    var shortCode : String,
    var ratePlanInclusion : ArrayList<InclusionPlan>,
    var roomBaseRate : String,
    var mealCharge : String,
    var inclusionCharge : String,
    var roundUp : String,
    var extraAdultRate : String,
    var extraChildRate : String,
    var ratePlanTotal : String,
    var mealPlanName : String,
)
data class InclusionPlan(
    val inclusionId: String,
    val inclusionType: String,
    val inclusionName: String,
    var postingRule: String,
    var chargeRule: String,
    var rate: String
)