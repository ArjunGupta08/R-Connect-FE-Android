package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType

import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsData

data class RatePlanDataClass(
    val ratePlan : String,
    val rateCode : String,
    val mealPlan : String,
    val selectedInclusionsList: ArrayList<GetInclusionsData>,
    val extraAdultRate : String,
    val extraChildRate : String,
    val ratePlanTotal : String
)
