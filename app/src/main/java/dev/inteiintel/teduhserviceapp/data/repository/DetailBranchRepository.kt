package dev.inteiintel.teduhserviceapp.data.repository

import dev.inteiintel.teduhserviceapp.data.model.Branch
import dev.inteiintel.teduhserviceapp.data.remote.ApiServices
import javax.inject.Inject

class DetailBranchRepository @Inject constructor(private val api: ApiServices) {

    suspend fun getBranchById(branchId: Int): Result<Branch> {
        return try {
            val result = api.getById(branchId)

            if (result.success) {
                Result.success(result.data) // atau langsung object
            } else {
                Result.failure(Exception("Gagal mengambil detail cabang $branchId"))
            }
        } catch (e: Exception){
            Result.failure(Exception(e.message))
        }

    }
}