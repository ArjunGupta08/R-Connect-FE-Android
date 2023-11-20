package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlaneDiscount

data class GetBarRatePlanForDiscountDataClass(
    val data : List<GetBarRate>
)

data class GetBarRate(
    val propertyId:String,
    val roomTypeId:String,
    val roomTypeName:String,
    val barRatePlans:List<GetBar>
)
data class GetBar(
    val ratePlanName:String,
    val barRatePlanId:String,
    val ratePlanTotal:String
)