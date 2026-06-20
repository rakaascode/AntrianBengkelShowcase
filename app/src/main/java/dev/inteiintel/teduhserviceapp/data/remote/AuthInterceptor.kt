package dev.inteiintel.teduhserviceapp.data.remote

import android.util.Log
import dev.inteiintel.teduhserviceapp.data.local.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

/**
 * OkHttp interceptor yang secara otomatis menyisipkan JWT ke header setiap request HTTP.
 *
 * Membaca access token dari [TokenManager] secara sinkron menggunakan `runBlocking`
 * (aman di thread OkHttp yang bukan Main thread). Jika token tidak kosong,
 * header `Authorization: Bearer <token>` ditambahkan ke request.
 *
 * Didaftarkan ke [ApiClient] saat pembuatan [OkHttpClient].
 *
 * @param tokenManager Manajer token untuk membaca access token dari DataStore.
 *
 * @see dev.inteiintel.teduhserviceapp.data.remote.ApiClient
 * @see dev.inteiintel.teduhserviceapp.data.local.TokenManager
 */
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