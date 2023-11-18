package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate

data class GetRoomType(
    val propertyId:String,
    val roomTypeId:String,
    val roomTypeName:String,
    val extraAdultRate:String,
    val extraChildRate:String
)
data class GetRoomTypeData(
    val `data`:ArrayList<GetRoomType>
)
