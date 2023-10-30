package rconnect.retvens.technologies

data class LoginResponse(
    val message: String,
    val statuscode: Int,
    val data: UserData?
)