package dev.inteiintel.teduhserviceapp.presentation.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.inteiintel.teduhserviceapp.data.model.UserData
import dev.inteiintel.teduhserviceapp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {
    private val _profile = MutableStateFlow<UserData?>(null)
    val getProfile = _profile.asStateFlow()

    fun loadProfile(){
        viewModelScope.launch {
            val result = userRepository.userProfile()

            result.onSuccess {
                _profile.value = it
            }.onFailure {
                // Handle error profile nanti lagi
                it.printStackTrace()
            }
        }
    }
}