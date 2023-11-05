package rconnect.retvens.technologies.dashboard.configuration.guestsAndReservation.reservationType

data class CreateReservationTypeDataClass(
    val userId : String,
    val propertyId : String,
    val reservationName : String,
    val status : String,
)

data class UpdateReservationTypeDataClass(
    val reservationName : String,
    val status : String,
)
