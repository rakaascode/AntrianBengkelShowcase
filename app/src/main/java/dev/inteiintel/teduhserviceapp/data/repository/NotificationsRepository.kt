package dev.inteiintel.teduhserviceapp.data.repository

import android.util.Log
import dev.inteiintel.teduhserviceapp.data.model.AntreanActiveData
import dev.inteiintel.teduhserviceapp.data.model.NotificationData
import dev.inteiintel.teduhserviceapp.data.model.NotificationResponse
import dev.inteiintel.teduhserviceapp.data.remote.ApiServices
import javax.inject.Inject

class NotificationsRepository @Inject constructor(private val api: ApiServices) {


    suspend fun getAllNotifications(): Result<List<NotificationData>> {

        return try {

            val response = api.getAllNotifications()

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