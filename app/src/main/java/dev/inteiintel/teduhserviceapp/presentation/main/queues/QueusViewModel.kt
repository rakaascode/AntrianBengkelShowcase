package dev.inteiintel.teduhserviceapp.presentation.main.queues

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.inteiintel.teduhserviceapp.data.model.AntreanActiveData
import dev.inteiintel.teduhserviceapp.data.repository.AntreanActiveRepository
import dev.inteiintel.teduhserviceapp.utils.NotificationHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QueusViewModel @Inject constructor(
    private val antreanRepository: AntreanActiveRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _antreanActive = MutableStateFlow<List<AntreanActiveData>>(emptyList())
    private val antreanActive = _antreanActive.asStateFlow()

    val antreanMenunggu = antreanActive.map { list ->
        list.filter { item ->
            item.status.equals("menunggu", ignoreCase = true) || item.status.equals("dipanggil", ignoreCase = true)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _loadingCancel = MutableStateFlow(false)
    val loadingCancel = _loadingCancel.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val notifiedCalledIds = mutableSetOf<Int>()
    private var pollingJob: Job? = null
    private val POLLING_INTERVAL_MS = 10_000L

    init {
        viewModelScope.launch {
            getAntreanActive()
        }
        startPolling()
    }

    fun startPolling() {
        pollingJob?.cancel()
        pollingJob = viewModelScope.launch {
            while (isActive) {
                delay(POLLING_INTERVAL_MS)
                getAntreanActive()
            }
        }
    }

    fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            val startTime = System.currentTimeMillis()
            getAntreanActive()
            val elapsed = System.currentTimeMillis() - startTime
            val minDelay = 650L
            if (elapsed < minDelay) {
                delay(minDelay - elapsed)
            }
            _isRefreshing.value = false
        }
    }

    suspend fun getAntreanActive() {
        try {
            val result = antreanRepository.getAntreanActive()

            result.onSuccess { data ->
                val prevMap = _antreanActive.value.associateBy { it.id }
                _antreanActive.value = data

                // Cek apakah ada antrean yang statusnya berubah menjadi 'dipanggil'
                for (item in data) {
                    val nomorDisplay = item.nomor_display ?: "A-%03d".format(item.nomor_antrian)
                    if (item.status.equals("dipanggil", ignoreCase = true) && !notifiedCalledIds.contains(item.id)) {
                        notifiedCalledIds.add(item.id)
                        NotificationHelper.showAntreanNotification(
                            context = context,
                            notificationId = item.id,
                            title = "Giliran Anda Tiba! 🔔",
                            message = "Nomor antrean #$nomorDisplay (${item.merk_motor} ${item.tipe_motor}) sedang dipanggil di loket servis. Segera menuju ke area servis!"
                        )
                    }
                }
            }

            result.onFailure { error ->
                Log.e("ERROR", error.message.toString())
            }

        } catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
        }
    }

    fun batalAntrean(
        id: Int,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {

            _loadingCancel.value = true

            antreanRepository.batalAntrean(id)
                .onSuccess {

                    getAntreanActive() // refresh
                    onSuccess(it)
                }
                .onFailure {
                    onError(it.message ?: "Gagal membatalkan")
                }

            _loadingCancel.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopPolling()
    }
}