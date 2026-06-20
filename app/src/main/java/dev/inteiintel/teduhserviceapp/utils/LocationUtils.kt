package dev.inteiintel.teduhserviceapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*

/**
 * Utilitas untuk mengambil posisi GPS pengguna saat ini.
 *
 * Digunakan oleh layar yang memerlukan lokasi pengguna, seperti [DaftarCabangScreen]
 * untuk mengurutkan cabang terdekat menggunakan [FindUtils.calculateDistanceKm].
 *
 * @see dev.inteiintel.teduhserviceapp.utils.FindUtils
 */
object LocationUtils {

    /**
     * Mendapatkan koordinat GPS pengguna saat ini secara asinkron.
     *
     * **Strategi dua tahap:**
     * 1. Mencoba `lastLocation` dari FusedLocationProvider (cepat, tanpa waktu tunggu).
     * 2. Jika `lastLocation` null (perangkat baru aktif), meminta satu update lokasi baru
     *    dengan prioritas akurasi tinggi sebagai fallback.
     *
     * Membutuhkan izin `ACCESS_FINE_LOCATION` atau `ACCESS_COARSE_LOCATION` yang sudah diberikan.
     *
     * @param context Konteks untuk inisialisasi [FusedLocationProviderClient].
     * @param onResult Callback dengan koordinat `(lat, lng)` saat lokasi berhasil didapat.
     * @param onError Callback tanpa parameter jika lokasi tidak dapat diperoleh.
     */
    @SuppressLint("MissingPermission")
    fun getUserLocation(
        context: Context,
        onResult: (lat: Double, lng: Double) -> Unit,
        onError: () -> Unit = {}
    ) {

        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(context)

        // 1️ Coba lastLocation dulu (cara kamu sekarang)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->

                if (location != null) {
                    onResult(location.latitude, location.longitude)
                } else {

                    // 2️ Fallback: request location update (INI YANG FIX NULL)
                    val request = LocationRequest.Builder(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        1000
                    ).setMaxUpdates(1).build()

                    val callback = object : LocationCallback() {
                        override fun onLocationResult(result: LocationResult) {
                            val loc = result.lastLocation

                            if (loc != null) {
                                onResult(loc.latitude, loc.longitude)
                            } else {
                                onError()
                            }

                            fusedLocationClient.removeLocationUpdates(this)
                        }
                    }

                    fusedLocationClient.requestLocationUpdates(
                        request,
                        callback,
                        Looper.getMainLooper()
                    )
                }
            }
            .addOnFailureListener {
                onError()
            }
    }
}