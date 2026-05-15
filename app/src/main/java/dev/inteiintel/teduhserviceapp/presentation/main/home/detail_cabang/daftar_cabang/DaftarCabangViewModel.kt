package dev.inteiintel.teduhserviceapp.presentation.main.home.detail_cabang.daftar_cabang

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.inteiintel.teduhserviceapp.data.model.Branch
import dev.inteiintel.teduhserviceapp.data.repository.BranchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DaftarCabangViewModel @Inject constructor(private val branchRepository: BranchRepository):
    ViewModel()  {
    private val _cabang = MutableStateFlow<List<Branch?>>(emptyList())
    val cabang = _cabang.asStateFlow()


    init {
        viewModelScope.launch {
            loadDataCabang()
        }
    }


    suspend fun loadDataCabang(){
        val result = branchRepository.getBranch()

        result.onSuccess { it ->
            _cabang.value = it
        }

        result.onFailure { it ->
            Log.d("ERROR:", it.message.toString())
        }
    }



}