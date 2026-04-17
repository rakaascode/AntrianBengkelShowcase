package dev.inteiintel.teduhserviceapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


private const val DATASTORE_NAME = "token_store"

val Context.tokenDataStore by preferencesDataStore(name = DATASTORE_NAME)

class TokenManager(private val context: Context){

    private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")


    /**
     * Menyimpan access token dan refresh token
     * @param accessToken token akses (token yang exp cepat)
     * @param refreshToken token yang masa exp lama
     */

    suspend fun simpanToken(
        context: Context,
        refreshToken: String
    ){
        context.tokenDataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    /**
     * Mendapatkan access token sebagai Flow (otomatis update jika berubah)
     * Cocok untuk diamati di UI
     */

    fun getAccessToken(): Flow<String>{
        return context.tokenDataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_KEY] ?: ""
        }
    }

    /**
     * Mendapatkan refresh token saat ini (sekali ambil)
     */

    fun getRefreshToken(): Flow<String> {
        return context.tokenDataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN_KEY] ?: ""
        }
    }

    /**
     * Mengecek apakah user sudah login atau belum (access token tidak boleh kosong)
     *
     */

    suspend fun isLoggedIn(): Boolean {
        val token = getRefreshToken().first()
        return token.isNotEmpty()
    }


    suspend fun hapusSemuaToken(){
        context.tokenDataStore.edit { pref ->
            pref.clear()
        }
    }

    suspend fun hapusAccessToken(): Preferences {
        return context.tokenDataStore.edit {  pref ->
            pref.remove(ACCESS_TOKEN_KEY)
        }
    }

    suspend fun hapusRefreshToken(): Preferences {
        return context.tokenDataStore.edit { pref ->
            pref.remove((REFRESH_TOKEN_KEY))
        }
    }





}
