package dev.inteiintel.teduhserviceapp.presentation.main.home.detail_cabang

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.inteiintel.teduhserviceapp.data.model.Branch
import dev.inteiintel.teduhserviceapp.data.model.RingkasanCabangItem
import dev.inteiintel.teduhserviceapp.data.repository.DetailBranchRepository
import dev.inteiintel.teduhserviceapp.data.repository.RingkasanHomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailCabangViewModel @Inject constructor(
    private val detailBranchRepository: DetailBranchRepository,
    private val ringkasanRepository: RingkasanHomeRepository

): ViewModel() {
    private val _dataBranch = MutableStateFlow<Branch?>(null)
    val dataBranch = _dataBranch.asStateFlow()

    private val _ringkasanCabang = MutableStateFlow<RingkasanCabangItem?>(null)
    val ringkasanCabang = _ringkasanCabang.asStateFlow()

    fun loadRingkasanCabang(branchId: Int) {

        viewModelScope.launch {

            ringkasanRepository.getRingkasanHome()
                .onSuccess { response ->

                    val cabang = response.data.find {
                        it.cabangId == branchId
                    }

                    _ringkasanCabang.value = cabang
                }
                .onFailure {

                    _ringkasanCabang.value = null
                }
        }
    }


    fun loadDataBranch(branchId: Int){
        viewModelScope.launch {
            try {
                val response = detailBranchRepository.getBranchById(branchId)
                response.onSuccess { it ->
                    _dataBranch.value = it
                }

                response.onFailure { it ->
                    Log.d("ERROR: ", it.message.toString())
                }
            }    catch (e: Exception){
                Log.d("ERROR: ", e.message.toString())
            }
        }

    }
}