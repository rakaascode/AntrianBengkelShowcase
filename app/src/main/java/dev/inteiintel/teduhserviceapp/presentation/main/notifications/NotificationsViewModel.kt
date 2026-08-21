package dev.inteiintel.teduhserviceapp.presentation.main.notifications

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.inteiintel.teduhserviceapp.data.model.NotificationData
import dev.inteiintel.teduhserviceapp.data.repository.NotificationsRepository
import dev.inteiintel.teduhserviceapp.utils.NotificationHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val notificationsRepository: NotificationsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _notificationsData = MutableStateFlow<List<NotificationData>>(emptyList())
    val getNotificationData = _notificationsData.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // Interval polling dalam milidetik (15 detik)
    private val POLLING_INTERVAL_MS = 15_000L

    private var pollingJob: Job? = null

    init {
        startPolling()
    }

    /** Mulai polling otomatis — load langsung + ulangi tiap 15 detik */
    fun startPolling() {
        pollingJob?.cancel()
        pollingJob = viewModelScope.launch {
            while (isActive) {
                loadAllNotifications()
                delay(POLLING_INTERVAL_MS)
            }
        }
    }

    /** Hentikan polling (dipanggil saat screen tidak aktif) */
    fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }

    /** Trigger refresh manual */
    fun refresh() {
        viewModelScope.launch {
            loadAllNotifications(isManualRefresh = true)
        }
    }

    private suspend fun loadAllNotifications(isManualRefresh: Boolean = false) {
        try {
            _isLoading.value = true
            val startTime = System.currentTimeMillis()
            val result = notificationsRepository.getAllNotifications()

            result.onSuccess { newList ->
                val prevIds = _notificationsData.value.map { it.id }.toSet()

                // Pertahankan status isRead lokal supaya tidak reset saat polling
                val currentReadIds = _notificationsData.value
                    .filter { it.isRead }
                    .map { it.id }
                    .toSet()

                _notificationsData.value = newList.map { item ->
                    if (item.id in currentReadIds) item.copy(isRead = true) else item
                }

                // Notifikasi sistem untuk item broadcast baru yang belum pernah diterima di sesi ini
                if (prevIds.isNotEmpty()) {
                    val newItems = newList.filter { it.id !in prevIds && !it.isRead }
                    for (item in newItems) {
                        NotificationHelper.showPromoNotification(
                            context = context,
                            notificationId = item.id,
                            title = item.judul ?: "Pemberitahuan Bengkel",
                            message = item.deskripsi ?: ""
                        )
                    }
                }
            }

            result.onFailure { error ->
                Log.e("NotificationsVM", "Load gagal: ${error.message}")
            }

            // Jika dipanggil via refresh manual, beri waktu berputar minimal 700ms agar animasi spinner terlihat jelas & smooth
            if (isManualRefresh) {
                val elapsed = System.currentTimeMillis() - startTime
                val minDelay = 700L
                if (elapsed < minDelay) {
                    delay(minDelay - elapsed)
                }
            }

        } catch (e: Exception) {
            Log.e("NotificationsVM", "Exception: ${e.message}")
        } finally {
            _isLoading.value = false
        }
    }

    fun markAsRead(id: Int) {
        _notificationsData.value = _notificationsData.value.map {
            if (it.id == id) it.copy(isRead = true) else it
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopPolling()
    }
}