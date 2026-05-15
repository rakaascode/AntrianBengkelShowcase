package dev.inteiintel.teduhserviceapp.data.repository

import dev.inteiintel.teduhserviceapp.data.model.UserData
import dev.inteiintel.teduhserviceapp.data.remote.ApiServices

class UserRepository(
    private val api: ApiServices
) {

    suspend fun userProfile(): Result<UserData>{
        return try {
            val response = api.getUserProfile()
            if (response.success){
                Result.success(response.data)
            }else{
                Result.failure(Exception("Gagal mengambil profile"))
            }
        } catch (e: Exception){
            Result.failure(e)
        }

    }

}