package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions

data class AddInclusionsDataClass(
    val userId : String,
    val propertyId : String,
    val shortCode : String,
    val charge : String,
    val inclusionName : String,
    val inclusionType : String,
    val chargeRule : String,
    val postingRule : String,
)

data class UpdateInclusionDataClass(
    val shortCode : String,
    val charge : String,
    val inclusionName : String,
    val inclusionType : String,
    val chargeRule : String,
    val postingRule : String,
)

data class GetInclusionsDataClass(
    val `data` : ArrayList<GetInclusionsData>,
    val message : String
)
data class GetInclusionsData(
    val propertyId : String,
    val inclusionId : String,
    val shortCode : String,
    val createdOn : String,
    val createdBy : String,
    val modifiedBy : String,
    val modifiedOn : String,
    val charge : String,
    val inclusionName : String,
    val inclusionType : String,
    val chargeRule : String,
    val postingRule : String,
)
