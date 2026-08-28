package dev.inteiintel.teduhserviceapp.utils

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

/**
 * Utilitas untuk mengeksekusi alur autentikasi Google Sign-In
 * menggunakan Legacy GoogleSignInClient (tidak memerlukan google-services.json).
 *
 * Mengembalikan ID token Google yang selanjutnya dikirim ke server backend
 * melalui [AuthRepository.loginGoogle] untuk mendapatkan JWT aplikasi.
 *
 * @see dev.inteiintel.teduhserviceapp.data.repository.AuthRepository
 * @see dev.inteiintel.teduhserviceapp.presentation.auth.AuthViewModel
 */
class GoogleAuthUtils {

    /**
     * Membuat [GoogleSignInClient] dengan konfigurasi Web Client ID.
     *
     * @param context Konteks Android.
     * @param webClientId OAuth 2.0 Web Client ID dari Google Cloud Console.
     */
    fun buildSignInClient(context: Context, webClientId: String): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }

    /**
     * Memproses hasil Intent dari [GoogleSignInClient.signInIntent] dan mengekstrak ID token.
     *
     * Dipanggil setelah pengguna memilih akun Google dari picker.
     *
     * @param data Intent hasil dari activity result launcher.
     * @return ID token Google sebagai String yang siap dikirim ke backend.
     * @throws ApiException jika proses sign-in gagal atau dibatalkan.
     */
    fun getIdTokenFromIntent(data: Intent?): String {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        val account = task.getResult(ApiException::class.java)
        return account.idToken ?: throw IllegalStateException("ID token kosong dari Google")
    }

    /**
     * Sign out dari GoogleSignInClient agar picker akun selalu muncul saat login berikutnya.
     *
     * @param context Konteks Android.
     * @param webClientId OAuth 2.0 Web Client ID.
     */
    fun signOut(context: Context, webClientId: String) {
        buildSignInClient(context, webClientId).signOut()
    }
}