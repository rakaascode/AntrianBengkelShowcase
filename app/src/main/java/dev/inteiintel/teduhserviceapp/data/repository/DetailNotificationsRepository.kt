package dev.inteiintel.teduhserviceapp.data.repository

import android.util.Log
import dev.inteiintel.teduhserviceapp.data.model.DetailNotificationData
import dev.inteiintel.teduhserviceapp.data.remote.ApiServices
import javax.inject.Inject

class DetailNotificationsRepository @Inject constructor(private val api: ApiServices) {

    suspend fun getDetailNotificationById(id: Int): Result<DetailNotificationData>{
        return try {
            val response = api.getNotificationsById(id)
            Result.success(response.data)
        } catch (e: Exception) {
            Log.d(
                "ERROR_ANTREAN_REPO",
                e.message.toString()
            )
            Result.failure(e)
        }
    }

}