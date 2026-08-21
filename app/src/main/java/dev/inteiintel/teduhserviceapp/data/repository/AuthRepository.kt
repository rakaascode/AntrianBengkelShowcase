package dev.inteiintel.teduhserviceapp.data.repository

import dev.inteiintel.teduhserviceapp.data.model.AuthResponse
import dev.inteiintel.teduhserviceapp.data.model.GoogleLoginRequest
import dev.inteiintel.teduhserviceapp.data.remote.ApiServices

class AuthRepository(
    private val api: ApiServices
) {

    suspend fun loginGoogle(idToken: String): Result<AuthResponse> {
        return try {

            val response = api.loginGoogle(
                GoogleLoginRequest(idToken)
            )

            if (response.isSuccessful) {

                val body = response.body()

                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Response kosong dari server"))
                }

            } else {
                Result.failure(
                    Exception("Error ${response.code()} - ${response.message()}")
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}