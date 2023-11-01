package rconnect.retvens.technologies.dashboard.configuration.reservation

data class AddReservationData(
    val userId : String,
    val propertyId : String,
    val reservationName : String,
    val status : String,
)
