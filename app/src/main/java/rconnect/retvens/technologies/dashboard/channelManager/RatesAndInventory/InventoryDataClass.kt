package rconnect.retvens.technologies.dashboard.channelManager.RatesAndInventory

data class InventoryDataClass(
    val date: String,
    val inventory: Int,
    val stopSell: String,
    val COA: String,
    val COD: String,
    val minimumLOS: String,
    val maximumLOS: String
)

data class RoomData(
    val roomTypeId: String,
    val roomTypeName: String,
    val numberOfRooms: Int,
    val calculatedInventoryData: List<InventoryDataClass>
)

data class ResponseData(
    val data: List<RoomData>,
    val statuscode: Int
)






