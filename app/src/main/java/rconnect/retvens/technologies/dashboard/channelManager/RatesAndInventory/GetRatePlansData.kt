package rconnect.retvens.technologies.dashboard.channelManager.RatesAndInventory

data class GetRatePlansData(
    val data: ArrayList<GetRatePlans>
)

data class GetRatePlans(
    val propertyId:String,
    val roomTypeId:String,
    val barRatePlanId:String,
    val ratePlanName:String
)
