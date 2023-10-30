package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlaneDiscount

data class RatePlanDiscountData(
    val selectedCount : Int,
    val roomType : String,
    val list : ArrayList<RatePlanRoomType>
)

data class RatePlanRoomType(
    val roomType : String
)
