package rconnect.retvens.technologies

data class LoginRequest(
    val username: String,
    val hotelCode: String,
    val password: String,
    val deviceType: String
)
data class UserData(
    val userId: String,
    val token: String
)

