package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar

data class GetMealData(
    val `data` : ArrayList<GetMealPlanItem>,
    val message : String
)

data class GetMealPlanItem(
    val propertyId : String,
    val mealPlanId : String,
    val shortCode : String,
    val createdOn : String,
    val createdBy : String,
    val modifiedBy : String,
    val modifiedOn : String,
    val mealPlanName : String,
    val chargesPerOccupancy : String,
)