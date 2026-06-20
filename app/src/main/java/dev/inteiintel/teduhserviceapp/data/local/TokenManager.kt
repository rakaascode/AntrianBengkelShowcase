package dev.inteiintel.teduhserviceapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "token_store"

val Context.tokenDataStore by preferencesDataStore(name = DATASTORE_NAME)

/**
 * Manajer token autentikasi berbasis Jetpack DataStore (Preferences).
 *
 * Menyimpan dan mengelola access token serta refresh token JWT secara persisten
 * di penyimpanan terenkripsi DataStore. Digunakan oleh:
 * - [AuthInterceptor] untuk mengambil access token pada setiap request API.
 * - [AuthViewModel] untuk menyimpan token setelah login Google berhasil.
 * - [AppNavGraph] dan [MainScreen] untuk mengecek status sesi (login/logout).
 *
 * DataStore didefinisikan melalui delegasi ekstensi [Context.tokenDataStore].
 *
 * @param context Konteks aplikasi untuk mengakses DataStore.
 *
 * @see dev.inteiintel.teduhserviceapp.data.remote.AuthInterceptor
 * @see dev.inteiintel.teduhserviceapp.presentation.auth.AuthViewModel
 */
class TokenManager(private val context: Context) {

    private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")

    /**
     * Simpan access + refresh token
     */
    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String
    ) {
        context.tokenDataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
    }



    /**
     * Simpan hanya refresh token
     */
    suspend fun saveRefreshToken(refreshToken: String) {
        context.tokenDataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    /**
     * Ambil access token (Flow)
     */
    fun getAccessToken(): Flow<String> {
        return context.tokenDataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_KEY] ?: ""
        }
    }

    /**
     * Ambil refresh token (Flow)
     */
    fun getRefreshToken(): Flow<String> {
        return context.tokenDataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN_KEY] ?: ""
        }
    }

    /**
     * Ambil access token sekali (untuk interceptor)
     */
    suspend fun getAccessTokenFirst(): String {
        return context.tokenDataStore.data.first()[ACCESS_TOKEN_KEY] ?: ""
    }

    /**
     * Check login status
     */
    suspend fun isLoggedIn(): Boolean {
        val token = context.tokenDataStore.data.first()[REFRESH_TOKEN_KEY]
        return !token.isNullOrEmpty()
    }

    /**
     * Hapus semua token
     */
    suspend fun clearAllToken() {
        context.tokenDataStore.edit { it.clear() }
    }

    /**
     * Hapus access token saja
     */
    suspend fun clearAccessToken() {
        context.tokenDataStore.edit { it.remove(ACCESS_TOKEN_KEY) }
    }

    /**
     * Hapus refresh token saja
     */
    suspend fun clearRefreshToken() {
        context.tokenDataStore.edit { it.remove(REFRESH_TOKEN_KEY) }
    }
}