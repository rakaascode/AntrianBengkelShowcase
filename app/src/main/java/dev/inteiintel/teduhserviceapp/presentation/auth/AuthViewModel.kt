package dev.inteiintel.teduhserviceapp.presentation.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dev.inteiintel.teduhserviceapp.R
import dev.inteiintel.teduhserviceapp.data.local.TokenManager
import dev.inteiintel.teduhserviceapp.data.model.OnBoarding
import dev.inteiintel.teduhserviceapp.utils.GoogleAuthUtils
import dev.inteiintel.teduhserviceapp.utils.NetworkStatus
import dev.inteiintel.teduhserviceapp.utils.NetworkUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class AuthViewModel: ViewModel() {

    val _getDataOnBoarding = MutableStateFlow<List<OnBoarding>>(emptyList())
    val getDataOnBoarding : StateFlow<List<OnBoarding>> =_getDataOnBoarding.asStateFlow()

    private val _token = MutableStateFlow<String?>(null)

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _accessToken = MutableLiveData<String>()
    val accessToken: LiveData<String> = _accessToken



    private val OAUTH_CLIENT_ID="""
        1077875078939-ba4fpbieucdqivm622c7udab9cim4ici.apps.googleusercontent.com
    """.trimIndent()


    init {
        loadData()
    }


    fun onLoginClick(context: Context, onError: (String)-> Unit){
        viewModelScope.launch {
            when(NetworkUtils.checkNetwork(context)){
                NetworkStatus.NoConnection -> onError("Tidak ada koneksi internet. Aktifkan WiFi atau data seluler 😊")
                NetworkStatus.NoInternet -> onError("Koneksi ada, tapi tidak bisa akses internet 👍")
                NetworkStatus.Available -> loginWithGoogle(context)
            }
        }

    }

    private fun loginWithGoogle(context: Context){
        viewModelScope.launch {
            _loading.value = true
            try {
                val idToken = GoogleAuthUtils().signInWithGoogle(context, OAUTH_CLIENT_ID)
                _token.value =idToken.toString()
                val token = _token.value
                saveTokenGoogle(context, token.toString())
            }catch (e: Exception){
                _token.value = "ERROR: ${e.message}"
            }
            _loading.value = false
        }
    }

    private fun saveTokenGoogle(context: Context, token: String){
        viewModelScope.launch {
            TokenManager(context).simpanToken(context,refreshToken = token)
        }
    }

    suspend fun getCurrentRefreshToken(context: Context):Flow<String> {
        return  TokenManager(context).getRefreshToken()
    }

    suspend fun isLoggedIn(context: Context): Boolean{
        return TokenManager(context).isLoggedIn()
    }

    suspend fun deleteToken(context: Context){
        return TokenManager(context).hapusSemuaToken()
    }


    fun loadData(){
        _getDataOnBoarding.value = listOf(
            OnBoarding(R.string.title_ob_satu,R.string.title_ob_satu_1, R.string.sub_title_ob_satu,R.drawable.img_ob_satu),
            OnBoarding(R.string.title_ob_dua, R.string.title_ob_dua_1, R.string.sub_title_ob_dua,R.drawable.img_ob_dua),
            OnBoarding(R.string.title_ob_tiga, R.string.title_ob_tiga_1,R.string.sub_title_ob_tiga,R.drawable.img_ob_tiga),
        )
    }
}