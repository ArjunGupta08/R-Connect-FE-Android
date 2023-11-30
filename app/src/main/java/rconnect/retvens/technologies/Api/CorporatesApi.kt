package rconnect.retvens.technologies.Api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import rconnect.retvens.technologies.dashboard.configuration.CorporateRates.AddCompany.AccountTypeDataClass
import rconnect.retvens.technologies.dashboard.configuration.CorporateRates.AddCompany.CompanyResponse
import rconnect.retvens.technologies.dashboard.configuration.CorporateRates.AddCompany.CorporatesDataClass
import rconnect.retvens.technologies.onboarding.ResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface CorporatesApi {
    @Multipart
    @POST("addCompany")
    fun addCompany(
        @Part("userId") userId: RequestBody,
        @Part("propertyId") propertyId: RequestBody,
        @Part companyLogo: MultipartBody.Part,
        @Part("companyName") companyName: RequestBody,
        @Part("accountType") accountType: RequestBody,
        @Part("companyEmail") companyEmail: RequestBody,
        @Part("companyWebsite") companyWebsite: RequestBody,
        @Part("shortCode") shortCode: RequestBody,
        @Part("registrationNumber") registrationNumber: RequestBody,
        @Part("taxId") taxId: RequestBody,
        @Part("openingBalance") openingBalance: RequestBody,
        @Part("month") month: RequestBody,
        @Part("days") day: RequestBody,
        @Part("contactPerson") contactPerson: RequestBody,
        @Part("phoneNumber") phoneNumber: RequestBody,
        @Part("personDesignation") personDesignation: RequestBody,
        @Part("personEmail") personEmail: RequestBody,
        @Part("addressLine1") addressLine1: RequestBody,
        @Part("addressLine2") addressLine2: RequestBody,
        @Part("country") country: RequestBody,
        @Part("state") state: RequestBody,
        @Part("city") city: RequestBody,
        @Part("zipCode") zipCode: RequestBody,
        @Part("creditLimit") creditLimit: RequestBody

    ): Call<CompanyResponse>

    @Multipart
    @POST("addCompany")
    fun addCompanyWithoutImage(
        @Part("userId") userId: RequestBody,
        @Part("propertyId") propertyId: RequestBody,
        @Part("companyName") companyName: RequestBody,
        @Part("accountType") accountType: RequestBody,
        @Part("companyEmail") companyEmail: RequestBody,
        @Part("companyWebsite") companyWebsite: RequestBody,
        @Part("shortCode") shortCode: RequestBody,
        @Part("registrationNumber") registrationNumber: RequestBody,
        @Part("taxId") taxId: RequestBody,
        @Part("openingBalance") openingBalance: RequestBody,
        @Part("month") month: RequestBody,
        @Part("days") day: RequestBody,
        @Part("contactPerson") contactPerson: RequestBody,
        @Part("phoneNumber") phoneNumber: RequestBody,
        @Part("personDesignation") personDesignation: RequestBody,
        @Part("personEmail") personEmail: RequestBody,
        @Part("addressLine1") addressLine1: RequestBody,
        @Part("addressLine2") addressLine2: RequestBody,
        @Part("country") country: RequestBody,
        @Part("state") state: RequestBody,
        @Part("city") city: RequestBody,
        @Part("zipCode") zipCode: RequestBody,
        @Part("creditLimit") creditLimit: RequestBody
    ): Call<CompanyResponse>

//    @Part("contractTerms") contractTerms: RequestBody,
//    @Part("effectiveFrom") effectiveFrom: RequestBody,
//    @Part("contractPdf") contractPdf: RequestBody,
//    @Part("expiration") expiration: RequestBody,

    @GET("getAccountType")
    fun getAccountType():Call<AccountTypeDataClass>

    @GET("companyType")
    fun getCorporates(
        @Query("propertyId")propertyId:String,
        @Query("userId")userId:String
    ):Call<CorporatesDataClass>
}