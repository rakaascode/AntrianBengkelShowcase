package dev.inteiintel.teduhserviceapp.presentation.main.home.detail_cabang.daftar_cabang

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.inteiintel.teduhserviceapp.data.model.Branch
import dev.inteiintel.teduhserviceapp.data.model.BranchWithDistance
import dev.inteiintel.teduhserviceapp.data.repository.BranchRepository
import dev.inteiintel.teduhserviceapp.utils.FindUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DaftarCabangViewModel @Inject constructor(
    private val branchRepository: BranchRepository
) : ViewModel() {

    // List cabang mentah (tanpa sort)
    private val _cabang = MutableStateFlow<List<Branch?>>(emptyList())
    val cabang = _cabang.asStateFlow()

    // List cabang yang sudah disort berdasarkan jarak (nullable jika lokasi belum tersedia)
    private val _cabangSorted = MutableStateFlow<List<BranchWithDistance>>(emptyList())
    val cabangSorted: StateFlow<List<BranchWithDistance>> = _cabangSorted.asStateFlow()

    // Apakah lokasi user sudah tersedia
    private val _hasLocation = MutableStateFlow(false)
    val hasLocation: StateFlow<Boolean> = _hasLocation.asStateFlow()

    init {
        viewModelScope.launch {
            loadDataCabang()
        }
    }

    suspend fun loadDataCabang() {
        val result = branchRepository.getBranch()

        result.onSuccess { list ->
            _cabang.value = list
        }

        result.onFailure { err ->
            Log.d("DaftarCabangVM", "ERROR: ${err.message}")
        }
    }

    /**
     * Dipanggil setelah mendapatkan lokasi user.
     * Mengurutkan semua cabang dari terdekat ke terjauh.
     */
    fun sortByDistance(userLat: Double, userLng: Double) {
        val rawList = _cabang.value.filterNotNull()

        if (rawList.isEmpty()) return

        val sorted = rawList
            .map { branch ->
                val lat = branch.latitude
                val lng = branch.longitude
                if (lat == null || lng == null) {
                    BranchWithDistance(branch, Double.MAX_VALUE)
                } else {
                    val dist = FindUtils.calculateDistanceKm(userLat, userLng, lat, lng)
                    BranchWithDistance(branch = branch, distanceKm = dist)
                }
            }
            .sortedBy { it.distanceKm ?: Double.MAX_VALUE }

        _cabangSorted.value = sorted
        _hasLocation.value = true
    }
}