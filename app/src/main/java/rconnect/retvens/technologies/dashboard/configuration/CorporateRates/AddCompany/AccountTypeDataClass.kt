package rconnect.retvens.technologies.dashboard.configuration.CorporateRates.AddCompany

data class AccountType(
    val accountTypeId: String,
    val accountType: String
)

data class AccountTypeDataClass(
    val data: List<AccountType>,
    val statusCode: Int
)
