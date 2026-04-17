package dev.inteiintel.teduhserviceapp.utils

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import javax.net.ssl.HttpsURLConnection


object NetworkUtils {
    fun isNetworkAvailable(context: Context): Boolean{
        val connectivityManager =context.getSystemService(ConnectivityManager::class.java)
        val network = connectivityManager.activeNetwork ?: return false
        val capability = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capability.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    suspend fun hasInternetCOnnection(): Boolean{
        return withContext (Dispatchers.IO){
            try {
                val url = URL("https://clients3.google.com/generate_204")
                val connection = url.openConnection() as HttpsURLConnection
                connection.setRequestProperty("User-Agent","Android")
                connection.setRequestProperty("Connection","close")
                connection.connectTimeout = 1500
                connection.connect()

                connection.responseCode == 204

            }catch (e: Exception){
                false
            }
        }
    }


    suspend fun checkNetwork(context: Context): NetworkStatus{
        if (!isNetworkAvailable(context)) return NetworkStatus.NoConnection
        if (!hasInternetCOnnection()) return NetworkStatus.NoInternet
        return NetworkStatus.Available
    }
}