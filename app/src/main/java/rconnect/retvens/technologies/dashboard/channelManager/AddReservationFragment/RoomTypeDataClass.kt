package rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment

data class RoomTypeDataClass(
    val data: List<RoomItem>,
    val statuscode: Int
)

data class RoomItem(
    val propertyId: String,
    var roomTypeId: String,
    val roomTypeName: String,
    var baseAdult: String,
    var baseChild: String
)
