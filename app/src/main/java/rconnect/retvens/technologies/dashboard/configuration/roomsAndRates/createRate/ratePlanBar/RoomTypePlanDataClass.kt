package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanBar

data class RoomTypePlanDataClass(
    val propertyId:String,
    val roomTypeId:String,
    val roomTypeName:String,
    val extraAdultRate:String,
    val extraChildRate:String,
    val roomBasePrice:String,
    val getMeal:ArrayList<GetMealPlanItem>
)
