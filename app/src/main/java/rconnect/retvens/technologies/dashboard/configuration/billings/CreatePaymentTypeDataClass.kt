package rconnect.retvens.technologies.dashboard.configuration.billings

data class AddPaymentTypeDataClass(
    val userId : String,
    val propertyId : String,
    val shortCode : String,
    val paymentMethodName : String,
    val receivedTo : String,
)

data class UpdatePaymentTypeDataClass(
    val shortCode : String,
    val paymentMethodName : String,
    val receivedTo : String,
)
