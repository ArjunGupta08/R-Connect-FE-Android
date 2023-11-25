package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany

import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsData

data class AddCompanyRatePlanDataClass(
    val userId : String,
    val propertyId : String,
    val roomTypeId:String,
    var rateType : String,
    val rateTypeId : String,
    val companyId : String,
    val ratePlanName : String,
    val mealPlanId : String,
    val shortCode : String,
    val ratePlanInclusion : ArrayList<InclusionPlan>,
    val roomBaseRate : String,
    val mealCharge : String,
    val inclusionCharge : String,
    val roundUp : String,
    val extraAdultRate : String,
    val extraChildRate : String,
    val ratePlanTotal : String,
    val mealPlanName : String,
)
data class InclusionPlan(
    val inclusionId: String,
    val inclusionType: String,
    val inclusionName: String,
    var postingRule: String,
    var chargeRule: String,
    var rate: String
)