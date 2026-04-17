package dev.inteiintel.teduhserviceapp.utils

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

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