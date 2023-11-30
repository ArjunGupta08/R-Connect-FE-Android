package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar

import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany.InclusionPlan
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsData

data class AddBarsRatePlanDataClass(
    var userId : String,
    var propertyId : String,
    var roomTypeId:String,
    var rateType : String,
    var rateTypeId : String,
    var companyId : String,
    var ratePlanName : String,
    var mealPlanId : String,
    var shortCode : String,
    var inclusionPlan : ArrayList<InclusionPlan>,
    var roomBaseRate : String,
    var mealCharge : String,
    var inclusionCharge : String,
    var roundUp : String,
    var extraAdultRate : String,
    var extraChildRate : String,
    var ratePlanTotal : String,
    var mealPlanName : String,
)
