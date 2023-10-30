package rconnect.retvens.technologies.onboarding.authentication

data class SignUpDataClass(
    val firstName: String,
    val lastName: String,
    val designation: String,
    val phoneNumber: String,
    val email: String,
    val password: String
)

data class SignUpResponse(
    val message:String
)