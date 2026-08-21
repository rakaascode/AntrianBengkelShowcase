package dev.inteiintel.teduhserviceapp.utils

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

/**
 * Utilitas untuk mengeksekusi alur autentikasi Google Sign-In
 * menggunakan Credential Manager API (Android 14+ / Jetpack Credential).
 *
 * Mengembalikan ID token Google yang selanjutnya dikirim ke server backend
 * melalui [AuthRepository.loginGoogle] untuk mendapatkan JWT aplikasi.
 *
 * @see dev.inteiintel.teduhserviceapp.data.repository.AuthRepository
 * @see dev.inteiintel.teduhserviceapp.presentation.auth.AuthViewModel
 */
class GoogleAuthUtils {

    private fun buildRequest(webClientId: String): GetCredentialRequest {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(webClientId)
            .setFilterByAuthorizedAccounts(false)
            .build()
        return GetCredentialRequest(
            listOf(googleIdOption)
        )
    }

    /**
     * Memulai alur Google Sign-In dan mengembalikan ID token.
     *
     * Menggunakan [CredentialManager] untuk menampilkan picker akun Google.
     * Jika pengguna membatalkan atau terjadi error, exception akan dilempar ke caller.
     *
     * @param context Konteks Activity/Application yang aktif.
     * @param webClientId OAuth 2.0 Web Client ID dari Google Cloud Console.
     * @return ID token Google sebagai String yang siap dikirim ke backend.
     * @throws androidx.credentials.exceptions.GetCredentialException jika login dibatalkan atau gagal.
     */
    suspend fun signInWithGoogle(
        context: Context,
        webClientId: String
    ): String {

        val credentialManager = CredentialManager.create(context)
        val request = buildRequest(webClientId)
        val result = credentialManager.getCredential(
            request = request,
            context = context
        )
        val credential = result.credential

        val googleIdTokenCredential = GoogleIdTokenCredential
            .createFrom(credential.data)
        return googleIdTokenCredential.idToken
    }
}