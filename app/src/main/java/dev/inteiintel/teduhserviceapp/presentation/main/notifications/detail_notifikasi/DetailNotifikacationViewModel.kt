package dev.inteiintel.teduhserviceapp.presentation.main.notifications.detail_notifikasi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.inteiintel.teduhserviceapp.data.model.DetailNotificationData
import dev.inteiintel.teduhserviceapp.data.repository.DetailNotificationsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailNotifikacationViewModel @Inject constructor(private val detailNotificationsRepository: DetailNotificationsRepository): ViewModel() {

    private val _dataDetailNotification = MutableStateFlow<DetailNotificationData?>(null)
    val getDataDetailNotification = _dataDetailNotification.asStateFlow()

    fun loadDetailNotifications(id: Int) {

        viewModelScope.launch {

            try {

                val result =
                    detailNotificationsRepository
                        .getDetailNotificationById(id)

                result.onSuccess {
                    _dataDetailNotification.value = it
                }

                result.onFailure {
                    Log.d(
                        "ERROR",
                        it.message.toString()
                    )
                }

            } catch (e: Exception) {
                Log.d(
                    "ERROR",
                    e.message.toString()
                )
            }
        }
    }

}