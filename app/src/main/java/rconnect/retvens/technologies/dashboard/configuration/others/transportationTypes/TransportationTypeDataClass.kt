package rconnect.retvens.technologies.dashboard.configuration.others.transportationTypes

data class AddTransportationTypeDataClass(
    val userId : String,
    val propertyId : String,
    val transportationModeName : String,
    val shortCode : String,
)

data class UpdateTransportationTypeDataClass(
    val shortCode : String,
    val transportationModeName : String,
)

data class GetTransportationTypeDataClass(
    val `data` : ArrayList<GetTransportationTypeData>,
    val message : String
)
data class GetTransportationTypeData(
    val createdOn : String,
    val createdBy : String,
    val propertyId : String,
    val transportationModeName : String,
    val transportationId : String,
    val modifiedBy : String,
    val modifiedOn : String,
    val shortCode : String,
)
