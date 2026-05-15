package dev.inteiintel.teduhserviceapp.data.repository

import dev.inteiintel.teduhserviceapp.data.model.ReminderModelsRequest
import dev.inteiintel.teduhserviceapp.data.model.ReminderModelsResponse
import dev.inteiintel.teduhserviceapp.data.remote.ApiServices
import javax.inject.Inject

class ReminderRepository @Inject constructor(
    private val apiService: ApiServices
) {

    suspend fun sendWhatsappReminder(
        noWa: String
    ): Result<ReminderModelsResponse> {
        return try {
            val response = apiService.updateNoWa(
                ReminderModelsRequest(no_wa = noWa)
            )

            if (response.success) {
                Result.success(response)
            } else {
                Result.failure(Exception(response.message))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}