package rconnect.retvens.technologies.onboarding.authentication

data class LoginRequest(
    val username: String,
    val hotelCode: String,
    val password: String,
    val deviceType: String
)

data class LoginResponse(
    val message: String,
    val statuscode: Int,
    val data: Data
)

data class Data(
    val userId: String,
    val token: String
)