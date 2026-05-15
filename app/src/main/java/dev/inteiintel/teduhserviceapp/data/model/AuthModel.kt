package dev.inteiintel.teduhserviceapp.data.model

data class GoogleLoginRequest(
    val id_token: String
)

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val data: AuthData
)

data class AuthData(
    val token: String,
    val user: User
)

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val role: String
)