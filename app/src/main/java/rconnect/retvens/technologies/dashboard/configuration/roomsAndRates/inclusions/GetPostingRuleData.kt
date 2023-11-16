package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions

data class GetPostingRuleData(
    val data : PostingRule
)

data class PostingRule(
    val postingRuleId:String,
    val postingRule:String
)
