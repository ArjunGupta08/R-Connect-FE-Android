package rconnect.retvens.technologies.dashboard.channelManager.RatesAndInventory

data class RoomType(
    val roomTypeId: String,
    val roomTypeName: String,
    val numberOfRooms: Int,
    val calculatedInventoryData: List<InventoryDataClass>
)

data class InventoryDataClass(
    val date: String,
    val inventory: Int,
    val isBlocked: String
)

data class ResponseData(
    val data: List<RoomType>,
    val statuscode: Int
)





