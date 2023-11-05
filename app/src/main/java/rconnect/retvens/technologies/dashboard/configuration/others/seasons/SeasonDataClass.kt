package rconnect.retvens.technologies.dashboard.configuration.others.seasons

data class AddSeasonDataClass(
    val userId : String,
    val propertyId : String,
    val seasonName : String,
    val shortCode : String,
    val startDate : String,
    val endDate : String,
    val days : ArrayList<String>
)

data class UpdateSeasonDataClass(
    val shortCode : String,
    val identityType : String,
)

data class GetSeasonDataClass(
    val `data` : ArrayList<GetSeasonData>,
    val message : String
)
data class GetSeasonData(
    val propertyId : String,
    val seasonId : String,
    val shortCode : String,
    val seasonName : String,
    val startDate : String,
    val endDate : String,
    val createdOn : String,
    val createdBy : String,
    val modifiedBy : String,
    val modifiedOn : String,
    val days : ArrayList<String>,
)
