package rconnect.retvens.technologies.dashboard.configuration.billings

data class GetPaymentTypeDataClass(
    val `data` : ArrayList<GetPaymentTypeData>,
    val message : String
)

data class GetPaymentTypeData(
    val createdOn : String,
    val createdBy : String,
    val paymentMethodName : String,
    val paymentTypeId : String,
    val modifiedBy : String,
    val modifiedOn : String,
    val receivedTo : String,
    val shortCode : String,
)
