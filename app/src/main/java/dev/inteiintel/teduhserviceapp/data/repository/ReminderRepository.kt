package dev.inteiintel.teduhserviceapp.data.repository

import dev.inteiintel.teduhserviceapp.data.model.ReminderModelsRequest
import dev.inteiintel.teduhserviceapp.data.model.ReminderModelsResponse
import dev.inteiintel.teduhserviceapp.data.remote.ApiServices
import javax.inject.Inject

class ReminderRepository @Inject constructor(
    private val apiService: ApiServices
) {

    /**
     * Menyimpan atau memperbarui nomor WhatsApp pengguna.
     *
     * Jika nomor WA belum ada → POST /users/kontak (simpan pertama kali).
     * Jika nomor WA sudah ada → PUT /users/kontak (update).
     *
     * @param noWa Nomor WhatsApp baru yang ingin disimpan/diperbarui.
     */
    suspend fun simpanAtauUpdateNoWa(
        noWa: String
    ): Result<ReminderModelsResponse> {
        return try {
            // Cek apakah nomor WA sudah tersimpan sebelumnya
            val existingResponse = apiService.getNoWa()
            val sudahAda = existingResponse.isSuccessful && existingResponse.body()?.success == true

            val response = if (sudahAda) {
                // Nomor WA sudah ada → gunakan PUT untuk update
                apiService.updateNoWa(ReminderModelsRequest(no_wa = noWa))
            } else {
                // Nomor WA belum ada → gunakan POST untuk simpan pertama kali
                apiService.simpanNoWa(ReminderModelsRequest(no_wa = noWa))
            }

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