package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar

import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.InclusionPlan
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsData

data class AddBarsRatePlanDataClass(
    val userId : String,
    val propertyId : String,
    val roomTypeId:String,
    var rateType : String,
    val rateTypeId : String,
    val companyId : String,
    val ratePlanName : String,
    val mealPlanId : String,
    val shortCode : String,
    var inclusionPlan : ArrayList<InclusionPlan>,
    val roomBaseRate : String,
    val mealCharge : String,
    val inclusionCharge : String,
    val roundUp : String,
    val extraAdultRate : String,
    val extraChildRate : String,
    val ratePlanTotal : String,
    val mealPlanName : String,
)
