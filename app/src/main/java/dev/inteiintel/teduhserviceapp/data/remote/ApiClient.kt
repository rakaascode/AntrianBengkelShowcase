package dev.inteiintel.teduhserviceapp.data.remote

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import dev.inteiintel.teduhserviceapp.data.local.TokenManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Factory object untuk membuat instance [ApiServices] yang telah dikonfigurasi dengan Retrofit.
 *
 * Secara otomatis menambahkan [AuthInterceptor] ke setiap request sehingga
 * header `Authorization: Bearer <token>` disertakan untuk endpoint yang terautentikasi.
 *
 * Base URL: `https://rakaascode.site/api/`
 *
 * @see dev.inteiintel.teduhserviceapp.data.remote.AuthInterceptor
 * @see dev.inteiintel.teduhserviceapp.data.remote.ApiServices
 * @see dev.inteiintel.teduhserviceapp.di.AppModule
 */
object ApiClient {

    /**
     * Membuat dan mengembalikan instance [ApiServices] yang siap digunakan.
     *
     * @param context Konteks untuk mengakses [TokenManager] melalui DataStore.
     * @return Instance [ApiServices] dengan interceptor autentikasi aktif.
     */
    fun create(context: Context): ApiServices {

        val tokenManager = TokenManager(context)

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .build()

        return Retrofit.Builder()
            .baseUrl("https://rakaascode.site/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices::class.java)
    }
}
