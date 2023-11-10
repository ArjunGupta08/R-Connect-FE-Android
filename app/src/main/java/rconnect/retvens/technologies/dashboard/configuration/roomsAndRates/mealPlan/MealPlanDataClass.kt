package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.mealPlan

data class MealPlanDataClass(
    val userId : String,
    val propertyId : String,
    val shortCode : String,
    val mealPlanName : String,
    val chargesPerOccupancy : String,
)

data class UpdateMealPlanData(
    val shortCode : String,
    val mealPlanName : String,
    val chargesPerOccupancy : String,
)

data class GetMealPlanDataClass(
    val `data` : ArrayList<GetMealPlanData>,
    val message : String
)
data class GetMealPlanData(
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
