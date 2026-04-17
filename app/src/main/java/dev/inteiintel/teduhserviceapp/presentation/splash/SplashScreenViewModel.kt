package dev.inteiintel.teduhserviceapp.presentation.splash

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.inteiintel.teduhserviceapp.data.local.TokenManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashScreenViewModel: ViewModel() {

    private val _isSplashScreenVisible = MutableStateFlow(true)
    val isSplashScreenVisible: StateFlow<Boolean> = _isSplashScreenVisible

    fun checkLogin(context: Context) {
        viewModelScope.launch {
            TokenManager(context).isLoggedIn() // cuma trigger read

            delay(300)
            _isSplashScreenVisible.value = false
        }
    }
}