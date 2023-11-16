package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanCompany

import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsData

data class AddCompanyRatePlanDataClass(
    val userId : String,
    val propertyId : String,
    val rateType : String,
    val rateTypeId : String,
    val companyId : String,
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
    val mealPlanName : String,
)
