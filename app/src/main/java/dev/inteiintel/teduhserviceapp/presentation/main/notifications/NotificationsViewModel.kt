package dev.inteiintel.teduhserviceapp.presentation.main.notifications

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.inteiintel.teduhserviceapp.data.model.NotificationData
import dev.inteiintel.teduhserviceapp.data.repository.NotificationsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(private val notificationsRepository: NotificationsRepository): ViewModel() {
    private val _notificationsData = MutableStateFlow<List<NotificationData>>(emptyList())
    val getNotificationData = _notificationsData.asStateFlow()

    init {
        viewModelScope.launch {
            loadAllNotifications()
        }
    }


    suspend fun loadAllNotifications(){
        try {
            val result = notificationsRepository.getAllNotifications()

            result.onSuccess { it ->
                _notificationsData.value = it
            }

            result.onFailure { error ->
                Log.e("ERROR", error.message.toString())
            }
        }catch (e: Exception){
                Log.e("ERROR", e.message.toString())
        }
    }

    fun markAsRead(id: Int) {

        _notificationsData.value =
            _notificationsData.value.map {

                if (it.id == id)
                    it.copy(isRead = true)
                else
                    it
            }
    }


}