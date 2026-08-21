package dev.inteiintel.teduhserviceapp.presentation.main.home.ambil_antrean.tambah_data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.inteiintel.teduhserviceapp.data.model.CreateAntrianRequest
import dev.inteiintel.teduhserviceapp.data.model.CreateAntrianResponse
import dev.inteiintel.teduhserviceapp.data.repository.AmbilAntreanRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TambahDataScreenViewModel @Inject constructor(
    private val ambilAntreanRepository: AmbilAntreanRepository
) : ViewModel() {



    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _antreanState = MutableStateFlow<CreateAntrianResponse?>(null)
    val antreanState = _antreanState.asStateFlow()

    fun createAntrean(antreanRequest: CreateAntrianRequest) {

        viewModelScope.launch {

            _isLoading.value = true
            _errorMessage.value = null
            delay(2000)

            try {

                val result = ambilAntreanRepository
                    .ambilAntreanUsers(antreanRequest)

                result.onSuccess { response ->
                    _antreanState.value = response
                    Log.d("SUCCESS", response.message)
                }

                result.onFailure { error ->
                    _errorMessage.value = error.message
                        ?: "Terjadi kesalahan"
                    Log.e("ERROR", error.message.toString())
                }

            } catch (e: Exception) {
                _errorMessage.value = e.message
                    ?: "Unknown error"
                Log.e("EXCEPTION", e.message.toString())

            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetState() {
        _antreanState.value = null
        _errorMessage.value = null
    }
}