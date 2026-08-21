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

/**
 * ViewModel untuk mengelola visibilitas splash screen.
 *
 * Mengecek status login melalui [TokenManager] dan menyembunyikan splash screen
 * setelah delay singkat 30ms. Digunakan oleh [MainActivity] melalui
 * `SplashScreen.setKeepOnScreenCondition`.
 *
 * @see dev.inteiintel.teduhserviceapp.MainActivity
 * @see dev.inteiintel.teduhserviceapp.data.local.TokenManager
 */
class SplashScreenViewModel : ViewModel() {

    private val _isSplashScreenVisible = MutableStateFlow(true)
    val isSplashScreenVisible: StateFlow<Boolean> = _isSplashScreenVisible

    /**
     * Memeriksa status login dan menyembunyikan splash screen setelah selesai.
     *
     * @param context Konteks untuk mengakses DataStore melalui [TokenManager].
     */
    fun checkLogin(context: Context) {
        viewModelScope.launch {
            TokenManager(context).isLoggedIn()

            delay(30)
            _isSplashScreenVisible.value = false
        }
    }
}