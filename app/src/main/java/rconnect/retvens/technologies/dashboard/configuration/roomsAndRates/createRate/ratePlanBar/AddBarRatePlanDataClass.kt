package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar

import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsData

data class AddBarRatePlanDataClass(
    val userId : String,
    val propertyId : String,
    val rateType : String,
    val rateTypeId : String,
    val ratePlanName : String,
    val mealPlanId : String,
    val shortCode : String,
    val ratePlanInclusion : ArrayList<GetInclusionsData>,
    val roomBaseRate : String,
    val mealCharge : String,
    val inclusionCharge : String,
    val roundUp : String,
    val extraAdultRate : String,
    val extraChildRate : String,
    val ratePlanTotal : String,
)
