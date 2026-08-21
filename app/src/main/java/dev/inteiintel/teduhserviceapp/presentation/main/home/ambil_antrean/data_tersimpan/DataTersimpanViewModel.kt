package dev.inteiintel.teduhserviceapp.presentation.main.home.ambil_antrean.data_tersimpan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.inteiintel.teduhserviceapp.data.local.room.SavedAntrianEntity
import dev.inteiintel.teduhserviceapp.data.model.ui.KendaraanTersimpanUiModel
import dev.inteiintel.teduhserviceapp.data.repository.SavedAntrianRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataTersimpanViewModel @Inject constructor(
    private val repository: SavedAntrianRepository
) : ViewModel() {

    private val _list = MutableStateFlow<List<SavedAntrianEntity>>(emptyList())
    val list = _list.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _list.value = repository.getAll()
        }
    }
}