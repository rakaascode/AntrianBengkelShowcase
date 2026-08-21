package dev.inteiintel.teduhserviceapp.presentation.main.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.inteiintel.teduhserviceapp.data.model.RingkasanCabangItem
import dev.inteiintel.teduhserviceapp.data.repository.RingkasanHomeRepository
import dev.inteiintel.teduhserviceapp.utils.FindUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RingkasanHomeViewModel @Inject constructor(
    private val repository: RingkasanHomeRepository
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _nearestCabang = MutableStateFlow<RingkasanCabangItem?>(null)
    val nearestCabang: StateFlow<RingkasanCabangItem?> = _nearestCabang

    private val _error = MutableStateFlow<String?>(null)

    fun getNearBranch(userLat: Double, userLng: Double) {
        viewModelScope.launch {
            _loading.value = true
            repository.getRingkasanHome()
                .onSuccess { response ->
                    val nearest = response.data.minByOrNull {
                        val distance = FindUtils.calculateDistanceKm(
                            userLat,
                            userLng,
                            it.latitude,
                            it.longitude
                        )
                        distance
                    }
                    _nearestCabang.value = nearest
                }
                .onFailure { error ->
                    _error.value = error.message
                }
            _loading.value = false
        }
    }

    /** Fallback: jika GPS tidak tersedia, pilih cabang yang punya antrian aktif */
    fun getNearBranchWithoutLocation() {
        viewModelScope.launch {
            _loading.value = true
            repository.getRingkasanHome()
                .onSuccess { response ->
                    // Pilih cabang dengan antrian aktif, atau cabang pertama sebagai default
                    val active = response.data.firstOrNull { it.sisaAntrian > 0 }
                        ?: response.data.firstOrNull()
                    _nearestCabang.value = active
                }
                .onFailure { error ->
                    _error.value = error.message
                }
            _loading.value = false
        }
    }
}