package dev.inteiintel.teduhserviceapp.presentation.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.inteiintel.teduhserviceapp.R
import dev.inteiintel.teduhserviceapp.data.local.TokenManager
import dev.inteiintel.teduhserviceapp.data.model.OnBoardingModel
import dev.inteiintel.teduhserviceapp.data.remote.ApiClient
import dev.inteiintel.teduhserviceapp.data.repository.AuthRepository
import dev.inteiintel.teduhserviceapp.utils.GoogleAuthUtils
import dev.inteiintel.teduhserviceapp.utils.NetworkStatus
import dev.inteiintel.teduhserviceapp.utils.NetworkUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


/** Sementara Hardcoded untuk testing, Nanti di ganti menggunakan Secrets Manager **/
private val OAUTH_CLIENT_ID =
    "1077875078939-ba4fpbieucdqivm622c7udab9cim4ici.apps.googleusercontent.com"


/**
 * ViewModel yang mengelola seluruh logika autentikasi Google OAuth dan status session.
 *
 * Bertanggung jawab untuk:
 * - Memuat data onboarding (slider konten selamat datang).
 * - Menjalankan alur login Google via [GoogleAuthUtils] dan menyimpan JWT ke [TokenManager].
 * - Mengecek status login (`isLoggedIn`) untuk menentukan start destination navigasi.
 * - Menghapus token saat logout.
 *
 * @see dev.inteiintel.teduhserviceapp.presentation.auth.AuthLoginScreen
 * @see dev.inteiintel.teduhserviceapp.data.local.TokenManager
 * @see dev.inteiintel.teduhserviceapp.data.repository.AuthRepository
 */
class AuthViewModel : ViewModel() {

    companion object {
        private const val TAG = "AUTH_VIEWMODEL"
    }

    private val _getDataOnBoardingModel = MutableStateFlow<List<OnBoardingModel>>(emptyList())
    val getDataOnBoardingModel: StateFlow<List<OnBoardingModel>> = _getDataOnBoardingModel.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _accessToken = MutableLiveData<String>()

    val googleAuthUtils = GoogleAuthUtils()

    init {
        Log.d(TAG, "ViewModel INIT → load onboarding data")
        loadData()
    }

    private fun createRepo(context: Context): AuthRepository {
        Log.d(TAG, "Creating AuthRepository")
        return AuthRepository(ApiClient.create(context))
    }

    /**
     * Membuat Intent untuk membuka picker akun Google.
     * Intent ini perlu di-launch dari [AuthLoginScreen] menggunakan [rememberLauncherForActivityResult].
     *
     * @param context Konteks Android.
     * @return Intent Google Sign-In yang siap di-launch.
     */
    fun getGoogleSignInIntent(context: Context) =
        googleAuthUtils.buildSignInClient(context, OAUTH_CLIENT_ID).signInIntent

    /**
     * Dipanggil setelah pengguna memilih akun Google dari picker (Activity Result).
     * Mengekstrak ID token dari intent, lalu mengirim ke backend untuk mendapatkan JWT.
     *
     * @param context Konteks Android.
     * @param data Intent hasil dari Activity Result launcher.
     * @param onError Callback dengan pesan error yang siap ditampilkan ke pengguna.
     */
    fun handleSignInResult(
        context: Context,
        data: android.content.Intent?,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _loading.value = true
            delay(1000)
            Log.d(TAG, "handleSignInResult called")
            try {
                val idToken = googleAuthUtils.getIdTokenFromIntent(data)
                Log.d(TAG, "ID token diterima, panjang: ${idToken.length}")

                val repo = createRepo(context)
                val result = repo.loginGoogle(idToken)

                result.onSuccess { response ->
                    val jwt = response.data.token
                    _accessToken.value = jwt
                    saveJwt(context, jwt)
                    Log.d(TAG, "Login Google berhasil")
                }

                result.onFailure { error ->
                    Log.e(TAG, "Login gagal dari backend: ${error.message}")
                    onError(error.message ?: "Login gagal")
                }

            } catch (e: Exception) {
                Log.e(TAG, "handleSignInResult exception: ${e.message}", e)
                val msg = if (e is com.google.android.gms.common.api.ApiException) {
                    "Google Sign-In Error: [${e.statusCode}] ${e.message}"
                } else {
                    e.message ?: "Login gagal"
                }
                onError(msg)
            }
            _loading.value = false
        }
    }

    /**
     * Memvalidasi koneksi internet terlebih dahulu sebelum memulai alur login Google.
     *
     * Menampilkan pesan error melalui [onError] jika tidak ada koneksi atau internet tidak
     * dapat dijangkau. Jika koneksi OK, memanggil [onReady] agar UI men-launch picker akun.
     *
     * @param context Konteks Android untuk cek konektivitas.
     * @param onError Callback dengan pesan error yang siap ditampilkan ke pengguna.
     * @param onReady Callback yang dipanggil saat koneksi OK — UI harus launch Google Sign-In intent.
     */
    fun onLoginClick(
        context: Context,
        onError: (String) -> Unit,
        onReady: () -> Unit
    ) {
        Log.d(TAG, "Login button clicked")
        _loading.value = true

        viewModelScope.launch {
            when (val status = NetworkUtils.checkNetwork(context)) {
                NetworkStatus.NoConnection -> {
                    Log.e(TAG, "NoConnection detected")
                    _loading.value = false
                    onError("Tidak ada koneksi internet. Aktifkan WiFi/data ")
                }

                NetworkStatus.NoInternet -> {
                    Log.e(TAG, "NoInternet detected")
                    _loading.value = false
                    onError("Koneksi ada tapi tidak bisa akses internet")
                }

                NetworkStatus.Available -> {
                    Log.d(TAG, "Network OK → launching Google Sign-In picker")
                    // Sign out dulu agar picker selalu muncul
                    googleAuthUtils.signOut(context, OAUTH_CLIENT_ID)
                    _loading.value = false
                    onReady()
                }
            }
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

    /**
     * Mengembalikan Flow refresh token dari DataStore.
     *
     * Digunakan oleh [AuthLoginScreen] dan [MainScreen] untuk mengawasi perubahan status
     * login secara reaktif — token kosong berarti sesi berakhir (logout otomatis).
     *
     * @param context Konteks yang diperlukan oleh [TokenManager].
     * @return [kotlinx.coroutines.flow.Flow] berisi string refresh token (kosong jika belum login).
     */
    fun getCurrentRefreshToken(context: Context) =
        TokenManager(context).getRefreshToken().also {
            Log.d(TAG, "Fetching refresh token flow")
        }

    /**
     * Memeriksa apakah pengguna sudah memiliki sesi aktif (refresh token tidak kosong).
     *
     * @param context Konteks untuk membaca DataStore.
     * @return `true` jika refresh token tersimpan, `false` jika belum login.
     */
    suspend fun isLoggedIn(context: Context): Boolean {
        val result = TokenManager(context).isLoggedIn()
        Log.d(TAG, "isLoggedIn = $result")
        return result
    }

    /**
     * Menghapus semua token dari DataStore — efektif melakukan logout.
     *
     * @param context Konteks untuk mengakses DataStore melalui [TokenManager].
     */
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