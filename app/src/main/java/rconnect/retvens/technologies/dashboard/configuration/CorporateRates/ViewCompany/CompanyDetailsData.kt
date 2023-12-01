package rconnect.retvens.technologies.dashboard.configuration.CorporateRates.ViewCompany

data class ContractPdf(
    val contractPdf: String,
    val uploadedBy: String
)

data class CompanyData(
    val propertyId: String,
    val companyName: String,
    val companyId: String,
    val accountType: String,
    val personEmail: String,
    val contractTerms: String,
    val contactPerson: String,
    val expiration: String,
    val companyLogo: String,
    val companyEmail: String,
    val companyWebsite: String,
    val shortCode: String,
    val registrationNumber: String,
    val taxId: String,
    val openingBalance: String,
    val creditLimit: String,
    val days: String,
    val month: String,
    val phoneNumber: String,
    val personDesignation: String,
    val contractPdf: List<ContractPdf>,
    val addressLine1: String,
    val addressLine2: String,
    val country: String,
    val state: String,
    val city: String,
    val zipCode: String,
    val effectiveFrom: String
)

data class CompanyDetailsData(
    val data: List<CompanyData>,
    val statuscode: Int
)
