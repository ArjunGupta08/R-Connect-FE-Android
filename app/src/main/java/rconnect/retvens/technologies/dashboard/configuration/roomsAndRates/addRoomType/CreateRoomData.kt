package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType

data class CreateRoomData(
    val userId : String,
    val propertyId : String,
    val baseAdult : String,
    val baseChild : String,
    val shortCode : String,
    val roomDescription : String,
    val roomTypeName : String,
    val maxAdult : String,
    val maxChild : String,
    val maxOccupancy : String,
    val noOfBeds : String,
    val bedTypeIds : String,
    val amenityIds : String,
    val deviceType : String,
)
//val baseRate : String,
//val extraAdultRate : String,
//val extraChildRate : String,
//val minimumRate : String,
//val maximumRate : String,