package dev.inteiintel.teduhserviceapp.utils

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

/**
 * Utilitas untuk mengeksekusi alur autentikasi Google Sign-In
 * menggunakan Credential Manager API (Standar modern Google Identity).
 *
 * @see dev.inteiintel.teduhserviceapp.data.repository.AuthRepository
 * @see dev.inteiintel.teduhserviceapp.presentation.auth.AuthViewModel
 */
class GoogleAuthUtils {

    /**
     * Memulai alur Google Sign-In menggunakan Credential Manager dan mengembalikan ID Token.
     *
     * @param context Konteks Android (Activity).
     * @param serverClientId OAuth 2.0 Web Client ID.
     * @return ID Token String.
     */
    suspend fun signInWithGoogle(
        context: Context,
        serverClientId: String
    ): String {
        val credentialManager = CredentialManager.create(context)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(serverClientId)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val response = credentialManager.getCredential(
            request = request,
            context = context
        )

        val credential = response.credential
        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
        return googleIdTokenCredential.idToken
    }
}