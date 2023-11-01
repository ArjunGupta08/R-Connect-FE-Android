package rconnect.retvens.technologies.dashboard.configuration.reservation

data class GetReservationTypeDataClass(
    val `data` : ArrayList<GetReservationTypeData>,
    val message : String
)

data class GetReservationTypeData(
    val propertyId : String,
    val reservationTypeId : String,
    val createdBy : String,
    val createdOn : String,
    val reservationName : String,
    val status : String,
    val modifiedBy : String,
    val modifiedOn : String,
)
