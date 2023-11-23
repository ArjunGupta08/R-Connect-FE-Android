package rconnect.retvens.technologies.dashboard.channelManager.RatesAndInventory

data class UpdateSingleInventory(
    val userId:String,
    val propertyId:String,
    val roomTypeId:String,
    val startDate:String,
    val endDate:String,
    val isAddedInventory:Boolean,
    val isBlockedInventory:Boolean,
    val inventory:String,
    val source:String
)
