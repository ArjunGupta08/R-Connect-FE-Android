package rconnect.retvens.technologies.dashboard.configuration.CorporateRates.AddCompany

data class Company(
    val propertyId: String,
    val companyName: String,
    val companyId: String,
    val contactPerson: String,
    val expiration: String,
    val ratePlans: Int
)

data class CorporatesDataClass(
    val data: List<Company>,
    val statuscode: Int
)

