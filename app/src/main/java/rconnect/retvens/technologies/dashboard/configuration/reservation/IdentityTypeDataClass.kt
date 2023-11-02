package rconnect.retvens.technologies.dashboard.configuration.reservation

data class AddIdentityTypeDataClass(
    val userId : String,
    val propertyId : String,
    val shortCode : String,
    val identityType : String,
)

data class UpdateIdentityTypeDataClass(
    val shortCode : String,
    val identityType : String,
)

data class GetIdentityTypeDataClass(
    val `data` : ArrayList<GetIdentityTypeData>,
    val message : String
)
data class GetIdentityTypeData(
    val createdOn : String,
    val createdBy : String,
    val shortCode : String,
    val identityType : String,
    val identityTypeId : String,
    val modifiedBy : String,
    val modifiedOn : String,
)
