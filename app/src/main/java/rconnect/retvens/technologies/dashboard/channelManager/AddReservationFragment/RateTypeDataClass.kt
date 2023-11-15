package rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment

data class RateType(
    val _id: String,
    val rateTypeId: String,
    val rateType: String
)

data class RateTypeDataClass(
    val data: List<RateType>,
    val statusCode: Int
)