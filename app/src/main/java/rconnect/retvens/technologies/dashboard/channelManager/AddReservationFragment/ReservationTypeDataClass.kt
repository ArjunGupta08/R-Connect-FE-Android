package rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment

data class ReservationTypeDataClass(
    val data: List<ReservationItem>,
    val statuscode: Int
)

data class ReservationItem(
    val _id: String,
    val propertyId: String,
    val reservationTypeId: String,
    val createdBy: String,
    val createdOn: String,
    val reservationName: String,
    val status: String,
    val modifiedBy: String,
    val modifiedOn: String,
    val __v: Int
)
