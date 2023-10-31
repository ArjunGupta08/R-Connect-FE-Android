package rconnect.retvens.technologies.dashboard.configuration.properties

data class FetchPropertyData(
    val `data` : ArrayList<PropData>
)

data class PropData(
    val country : String,
    val createdOn : String,
    val userId : String,
    val propertyId : String,
)