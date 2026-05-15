package dev.inteiintel.teduhserviceapp.data.repository

import dev.inteiintel.teduhserviceapp.data.model.Branch
import dev.inteiintel.teduhserviceapp.data.remote.ApiServices

class BranchRepository(private val api: ApiServices) {

    suspend fun getBranch(): Result<List<Branch>>{
        return try {
            val response = api.getAllBranch()

            if (response.success){
                Result.success(response.data)
            } else{
                Result.failure(Exception(response.message))
            }

        }catch (e: Exception){
            Result.failure(e)
        }
    }

}