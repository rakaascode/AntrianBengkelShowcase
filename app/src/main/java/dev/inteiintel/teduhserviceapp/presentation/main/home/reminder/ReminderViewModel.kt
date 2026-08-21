package dev.inteiintel.teduhserviceapp.presentation.main.home.reminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.inteiintel.teduhserviceapp.data.model.ReminderModelsResponse
import dev.inteiintel.teduhserviceapp.data.repository.ReminderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ReminderState {
    object Idle : ReminderState()
    object Loading : ReminderState()
    data class Success(val data: ReminderModelsResponse) : ReminderState()
    data class Error(val message: String) : ReminderState()
}

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ReminderState>(ReminderState.Idle)
    val state: StateFlow<ReminderState> = _state

    private val _savedWa = MutableStateFlow<String?>(null)
    val savedWa: StateFlow<String?> = _savedWa

    fun loadSavedWa() {
        viewModelScope.launch {
            val result = reminderRepository.getNoWa()
            result.onSuccess { response ->
                _savedWa.value = response?.data?.no_wa
            }
        }
    }

    // FUNCTION KIRIM WHATSAPP
    fun sendWhatsapp(noWa: String) {
        viewModelScope.launch {
            _state.value = ReminderState.Loading
            val result = reminderRepository.simpanAtauUpdateNoWa(noWa)
            result.onSuccess { response ->
                _savedWa.value = response.data.no_wa
                _state.value = ReminderState.Success(response)
            }.onFailure { error ->
                _state.value = ReminderState.Error(
                    error.message ?: "Gagal menyimpan nomor WhatsApp"
                )
            }
        }
    }

    fun resetState() {
        _state.value = ReminderState.Idle
    }
}