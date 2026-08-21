package dev.inteiintel.teduhserviceapp.presentation.main.queues

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.inteiintel.teduhserviceapp.data.model.AntreanActiveData
import dev.inteiintel.teduhserviceapp.data.repository.AmbilAntreanRepository
import dev.inteiintel.teduhserviceapp.data.repository.AntreanActiveRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QueusViewModel @Inject constructor(
    private val antreanRepository: AntreanActiveRepository
) : ViewModel() {

    private val _antreanActive = MutableStateFlow<List<AntreanActiveData>>(emptyList())
    private val antreanActive = _antreanActive.asStateFlow()

    val antreanMenunggu = antreanActive.map { list ->
        list.filter { item ->
            item.status.equals("menunggu", ignoreCase = true)
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

    init {
        viewModelScope.launch {
            getAntreanActive()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            val startTime = System.currentTimeMillis()
            getAntreanActive()
            val elapsed = System.currentTimeMillis() - startTime
            val minDelay = 650L
            if (elapsed < minDelay) {
                kotlinx.coroutines.delay(minDelay - elapsed)
            }
            _isRefreshing.value = false
        }
    }

    suspend fun getAntreanActive() {
        try {
            val result = antreanRepository.getAntreanActive()

            result.onSuccess { data ->
                _antreanActive.value = data
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
}