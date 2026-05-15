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

    fun getRingkasanHome(userLat: Double, userLng: Double) {

        viewModelScope.launch {

            _loading.value = true

            Log.d("RINGKASAN_VM", "🚀 REQUEST START")
            Log.d("RINGKASAN_VM", "📍 User Location: lat=$userLat lng=$userLng")

            repository.getRingkasanHome()
                .onSuccess { response ->

                    Log.d("RINGKASAN_VM", "✅ API SUCCESS")
                    Log.d("RINGKASAN_VM", "📦 Total cabang: ${response.data.size}")

                    response.data.forEach {
                        Log.d(
                            "RINGKASAN_VM",
                            "🏢 Cabang: ${it.namaCabang} | lat=${it.latitude} lng=${it.longitude}"
                        )
                    }

                    val nearest = response.data.minByOrNull {
                        val distance = FindUtils.calculateDistanceKm(
                            userLat,
                            userLng,
                            it.latitude,
                            it.longitude
                        )

                        Log.d(
                            "RINGKASAN_VM",
                            "📏 ${it.namaCabang} distance = $distance km"
                        )

                        distance
                    }

                    Log.d(
                        "RINGKASAN_VM",
                        "🎯 NEAREST CABANG = ${nearest?.namaCabang}"
                    )

                    _nearestCabang.value = nearest
                }
                .onFailure { error ->

                    Log.e("RINGKASAN_VM", "❌ ERROR: ${error.message}")
                    _error.value = error.message
                }

            _loading.value = false

            Log.d("RINGKASAN_VM", "🏁 REQUEST END")
        }
    }
}