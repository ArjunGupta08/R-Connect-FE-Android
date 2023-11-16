package rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment

data class AvailableRoomType(
    val roomTypeId: String,
    val roomTypeName: String,
    val baseAdult: String,
    val maxChild: String,
    val maxAdult: String,
    val baseChild: String,
    val minimumInventory: Int
)

data class AvailableRoomDataClass(
    val data: List<AvailableRoomType>,
    val statuscode: Int
)