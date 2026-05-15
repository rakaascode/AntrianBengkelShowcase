package dev.inteiintel.teduhserviceapp.data.remote

import android.util.Log
import dev.inteiintel.teduhserviceapp.data.local.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val token = runBlocking {
            tokenManager.getAccessTokenFirst()
        }

        Log.d("AUTH_TOKEN_USED", token)

        val request = chain.request().newBuilder()
            .apply {
                if (token.isNotEmpty()) {
                    addHeader("Authorization", "Bearer $token")
                }
            }
            .build()

        return chain.proceed(request)
    }
}