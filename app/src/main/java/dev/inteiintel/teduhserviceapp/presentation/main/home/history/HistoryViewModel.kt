package dev.inteiintel.teduhserviceapp.presentation.main.home.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.inteiintel.teduhserviceapp.data.model.Antrian
import dev.inteiintel.teduhserviceapp.data.model.ui.AntreanSuccess
import dev.inteiintel.teduhserviceapp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel(){

    private var _riwayatAntrean = MutableStateFlow<List<Antrian>>(emptyList())
    val dataRiwayatAntrean = _riwayatAntrean.asStateFlow()


     fun loadUserData(){
         viewModelScope.launch {
             try {
                 val result = userRepository.userProfile()

                 result.onSuccess { it ->
                     _riwayatAntrean.value = it.antrian
                 }

                 result.onFailure { it ->
                     Log.d("ERROR: ", it.message.toString())
                 }
             }catch (e: Exception){
                 Log.d("ERROR HISTORY MODEL:",e.message.toString())
             }
         }
    }


}