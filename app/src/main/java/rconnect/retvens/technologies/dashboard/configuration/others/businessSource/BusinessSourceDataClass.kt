package rconnect.retvens.technologies.dashboard.configuration.others.businessSource

data class AddBusinessSourceDataClass(
    val userId : String,
    val shortCode:String,
    val propertyId : String,
    val sourceName: String
)

data class UpdateBusinessSourceDataClass(
    val shortCode : String,
    val sourceName : String,
)

data class GetBusinessSourceDataClass(
    val `data` : ArrayList<GetBusinessSourceData>
)
data class GetBusinessSourceData(
    val createdOn : String,
    val sourceId:String,
    val createdBy : String,
    val sourceName:String,
    val modifiedBy:String,
    val modifiedOn:String,
    val shortCode:String
)
