package rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment

data class BookingSorceDataClass(
    val data: List<BookingItem>,
    val statusCode: Int
)

data class BookingItem(
    val createdOn: String,
    val bookingSourceId: String,
    val createdBy: String,
    val bookingSource: String,
    val modifiedBy: String,
    val modifiedOn: String,
    val shortCode: String
)
