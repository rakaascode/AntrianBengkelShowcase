package dev.inteiintel.teduhserviceapp.data.repository

import dev.inteiintel.teduhserviceapp.data.model.RingkasanHomeResponse
import dev.inteiintel.teduhserviceapp.data.remote.ApiServices
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RingkasanHomeRepository @Inject constructor(
    private val api: ApiServices
) {


    suspend fun getRingkasanHome(): Result<RingkasanHomeResponse> {
        return try {

            val result = api.getAllRingkasan()

            Result.success(result)

        } catch (e: HttpException) {

            Result.failure(
                Exception("HTTP Error: ${e.code()}")
            )

        } catch (e: IOException) {

            Result.failure(
                Exception("Koneksi internet bermasalah")
            )

        } catch (e: Exception) {

            Result.failure(
                Exception(e.message ?: "Terjadi kesalahan")
            )
        }
    }
}