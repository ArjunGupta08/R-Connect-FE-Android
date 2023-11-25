package rconnect.retvens.technologies.dashboard.configuration.properties

data class FetchPropertyData(
    val `data` : ArrayList<PropData>,
    val message : String
)

data class PropData(
    val propertyName : String,
    val propertyId : String,
    val propertyType : String,
    val city : String,
    val country : String,
    val hotelLogo : String,
    val propertyRating : String,
    val createdOn : String,
    val totalRooms : String,
    val amenities : String,
)