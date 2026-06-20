package dev.inteiintel.teduhserviceapp.utils

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import javax.net.ssl.HttpsURLConnection


/**
 * Kumpulan utilitas untuk memeriksa konektivitas dan ketersediaan internet.
 *
 * Digunakan oleh [AuthViewModel] sebelum memulai proses login untuk memastikan
 * tidak ada kegagalan jaringan yang tidak perlu.
 *
 * @see dev.inteiintel.teduhserviceapp.utils.NetworkStatus
 */
object NetworkUtils {

    /**
     * Memeriksa apakah perangkat terhubung ke jaringan apa pun (WiFi, data seluler, dll.).
     *
     * @param context Konteks Android untuk mengakses [android.net.ConnectivityManager].
     * @return `true` jika ada jaringan aktif dengan kapabilitas internet.
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
        val network = connectivityManager.activeNetwork ?: return false
        val capability = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capability.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    /**
     * Memverifikasi koneksi internet nyata dengan melakukan ping ke server Google (204).
     *
     * Berjalan di [kotlinx.coroutines.Dispatchers.IO] dengan timeout 1,5 detik.
     *
     * @return `true` jika server merespons dengan HTTP 204.
     */
    suspend fun hasInternetCOnnection(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("https://clients3.google.com/generate_204")
                val connection = url.openConnection() as HttpsURLConnection
                connection.setRequestProperty("User-Agent", "Android")
                connection.setRequestProperty("Connection", "close")
                connection.connectTimeout = 1500
                connection.connect()

                connection.responseCode == 204

            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Menggabungkan cek jaringan dan cek internet menjadi satu [NetworkStatus].
     *
     * @param context Konteks untuk mengakses system service jaringan.
     * @return [NetworkStatus.Available], [NetworkStatus.NoConnection], atau [NetworkStatus.NoInternet].
     */
    suspend fun checkNetwork(context: Context): NetworkStatus {
        if (!isNetworkAvailable(context)) return NetworkStatus.NoConnection
        if (!hasInternetCOnnection()) return NetworkStatus.NoInternet
        return NetworkStatus.Available
    }
}