package dev.inteiintel.teduhserviceapp.presentation.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import dev.inteiintel.teduhserviceapp.R
import dev.inteiintel.teduhserviceapp.data.local.TokenManager
import dev.inteiintel.teduhserviceapp.data.model.OnBoardingModel
import dev.inteiintel.teduhserviceapp.data.remote.ApiClient
import dev.inteiintel.teduhserviceapp.data.repository.AuthRepository
import dev.inteiintel.teduhserviceapp.utils.GoogleAuthUtils
import dev.inteiintel.teduhserviceapp.utils.NetworkStatus
import dev.inteiintel.teduhserviceapp.utils.NetworkUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class AuthViewModel : ViewModel() {

    companion object {
        private const val TAG = "AUTH_VIEWMODEL"
    }

    private val _getDataOnBoardingModel = MutableStateFlow<List<OnBoardingModel>>(emptyList())
    val getDataOnBoardingModel: StateFlow<List<OnBoardingModel>> = _getDataOnBoardingModel.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _accessToken = MutableLiveData<String>()
    val accessToken: LiveData<String> = _accessToken

    private val OAUTH_CLIENT_ID =
        "1077875078939-ba4fpbieucdqivm622c7udab9cim4ici.apps.googleusercontent.com"

    init {
        Log.d(TAG, "ViewModel INIT → load onboarding data")
        loadData()
    }

    private fun createRepo(context: Context): AuthRepository {
        Log.d(TAG, "Creating AuthRepository")
        return AuthRepository(ApiClient.create(context))
    }

    fun onLoginClick(context: Context, onError: (String) -> Unit) {
        Log.d(TAG, "Login button clicked")

        viewModelScope.launch {
            when (val status = NetworkUtils.checkNetwork(context)) {
                NetworkStatus.NoConnection -> {
                    Log.e(TAG, "NoConnection detected")
                    onError("Tidak ada koneksi internet. Aktifkan WiFi/data 📵")
                }

                NetworkStatus.NoInternet -> {
                    Log.e(TAG, "NoInternet detected")
                    onError("Koneksi ada tapi tidak bisa akses internet ⚠️")
                }

                NetworkStatus.Available -> {
                    Log.d(TAG, "Network OK → starting Google login")
                    loginWithGoogle(context, onError)
                }
            }
        }
    }

    private fun loginWithGoogle(context: Context, onError: (String) -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            Log.d(TAG, "LOGIN STARTED")

            try {
                Log.d(TAG, "Requesting Google ID Token...")

                val idToken = GoogleAuthUtils()
                    .signInWithGoogle(context, OAUTH_CLIENT_ID)

                Log.d(TAG, "Google ID Token received: ${idToken}")

                val repo = createRepo(context)

                Log.d(TAG, "Sending token to backend API")
                val result = repo.loginGoogle(idToken)

                result.onSuccess { response ->
                    Log.d(TAG, "API SUCCESS RESPONSE: $response")

                    val jwt = response.data.token
                    Log.d(TAG, "JWT RECEIVED: $jwt")

                    _accessToken.value = jwt
                    Log.d(TAG, "LiveData updated with JWT")

                    saveJwt(context, jwt)
                }

                result.onFailure { error ->
                    Log.e(TAG, "API LOGIN FAILED", error)
                    onError(error.message ?: "Login gagal")
                }

            } catch (e: Exception) {
                Log.e(TAG, "LOGIN EXCEPTION", e)
                onError(e.message ?: "Unknown error")
            }

            _loading.value = false
            Log.d(TAG, "LOGIN FINISHED")
        }
    }

    private fun saveJwt(context: Context, jwt: String) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Saving JWT to DataStore...")

                TokenManager(context).saveTokens(jwt, jwt)

                Log.d(TAG, "JWT SAVED SUCCESSFULLY")
            } catch (e: Exception) {
                Log.e(TAG, "FAILED TO SAVE JWT", e)
            }
        }
    }

    fun getCurrentRefreshToken(context: Context) =
        TokenManager(context).getRefreshToken().also {
            Log.d(TAG, "Fetching refresh token flow")
        }

    suspend fun isLoggedIn(context: Context): Boolean {
        val result = TokenManager(context).isLoggedIn()
        Log.d(TAG, "isLoggedIn = $result")
        return result
    }

    fun deleteToken(context: Context) {
        viewModelScope.launch {
            Log.d(TAG, "Clearing all tokens")
            TokenManager(context).clearAllToken()
        }
    }

    fun loadData() {
        Log.d(TAG, "Loading onboarding data")

        _getDataOnBoardingModel.value = listOf(
            OnBoardingModel(
                R.string.title_ob_satu,
                R.string.title_ob_satu_1,
                R.string.sub_title_ob_satu,
                R.drawable.img_ob_satu
            ),
            OnBoardingModel(
                R.string.title_ob_dua,
                R.string.title_ob_dua_1,
                R.string.sub_title_ob_dua,
                R.drawable.img_ob_dua
            ),
            OnBoardingModel(
                R.string.title_ob_tiga,
                R.string.title_ob_tiga_1,
                R.string.sub_title_ob_tiga,
                R.drawable.img_ob_tiga
            ),
        )

        Log.d(TAG, "Onboarding data loaded: ${_getDataOnBoardingModel.value.size} items")
    }
}