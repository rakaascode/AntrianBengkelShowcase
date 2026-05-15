package dev.inteiintel.teduhserviceapp.data.repository

import android.util.Log
import dev.inteiintel.teduhserviceapp.data.model.AntreanActiveData
import dev.inteiintel.teduhserviceapp.data.model.AntreanActiveModelResponse
import dev.inteiintel.teduhserviceapp.data.remote.ApiServices
import javax.inject.Inject

class AntreanActiveRepository @Inject constructor(
    private val api: ApiServices
) {

    suspend fun getAntreanActive(): Result<List<AntreanActiveData>> {

        return try {

            val response = api.getAntreanActive()

            Result.success(response.data)

        } catch (e: Exception) {

            Log.d(
                "ERROR_ANTREAN_REPO",
                e.message.toString()
            )

            Result.failure(e)
        }
    }


    suspend fun batalAntrean(
        id: Int
    ): Result<String> {

        return try {

            val response = api.batalAntrean(id)

            if (response.isSuccessful) {

                Result.success(
                    response.body()?.message ?: "Berhasil"
                )

            } else {

                Result.failure(
                    Exception(
                        response.body()?.message
                            ?: "Gagal membatalkan antrean"
                    )
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}