package dev.inteiintel.teduhserviceapp.data.repository

import dev.inteiintel.teduhserviceapp.data.model.CreateAntrianRequest
import dev.inteiintel.teduhserviceapp.data.model.CreateAntrianResponse
import dev.inteiintel.teduhserviceapp.data.remote.ApiServices
import javax.inject.Inject

class AmbilAntreanRepository @Inject constructor (private val api: ApiServices){

    suspend fun ambilAntreanUsers(DataAntrean: CreateAntrianRequest): Result<CreateAntrianResponse>{
        return try {
            val response = api.ambilAntreanUsers(DataAntrean)

            if (response.isSuccessful){
                val body = response.body()

                if (body != null){
                    Result.success(body)
                } else {
                    Result.failure(Exception(response.message()))
                }

            } else {
                Result.failure(
                    Exception("Error ${response.code()} - ${response.message()}")
                )
            }
        }catch (e: Exception){
            Result.failure(Exception(e.message))
        }
    }

}