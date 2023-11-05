package rconnect.retvens.technologies.dashboard.configuration.properties

import org.json.JSONArray

data class FetchPropertyData(
    val `data` : ArrayList<PropData>,
    val message : String
)

data class PropData(
    val country : String,
    val createdOn : String,
    val userId : String,
    val propertyId : String,
)