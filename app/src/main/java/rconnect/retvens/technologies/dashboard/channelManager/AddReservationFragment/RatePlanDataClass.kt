package rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment

data class RatePlanDataClass(
    val data: List<RatePlanItem>,
    val statuscode: Int
)

data class RatePlanItem(
    val propertyId: String,
    val roomTypeId: String,
    val roomTypeName: String,
    val barRatePlanId: String,
    val ratePlanTotal: String,
    val extraChildRate: String,
    val extraAdultRate: String,
    val ratePlanName: String
)
