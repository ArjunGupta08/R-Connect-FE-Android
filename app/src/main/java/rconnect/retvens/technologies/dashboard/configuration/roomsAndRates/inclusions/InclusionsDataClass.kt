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
    val propertyId : String ?= "",
    var inclusionId : String,
    val shortCode : String ?= "",
    val createdOn : String ?= "",
    val createdBy : String ?= "",
    val modifiedBy : String ?= "",
    val modifiedOn : String ?= "",
    var charge : String,
    var inclusionName : String,
    var inclusionType : String,
    var chargeRule : String,
    var postingRule : String,
)

data class GetPostingRuleArray (
    val `data` : ArrayList<GetPostingRuleData>
)
data class GetPostingRuleData (
    val postingRuleId : String,
    val postingRule : String,
)

data class GetChargeRuleArray (
    val `data` : ArrayList<GetChargeRuleData>
)
data class GetChargeRuleData (
    val chargeRuleId : String,
    val chargeRule : String,
)