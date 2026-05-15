package dev.inteiintel.teduhserviceapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*

object LocationUtils {

    @SuppressLint("MissingPermission")
    fun getUserLocation(
        context: Context,
        onResult: (lat: Double, lng: Double) -> Unit,
        onError: () -> Unit = {}
    ) {

        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(context)

        // 1️⃣ Coba lastLocation dulu (cara kamu sekarang)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->

                if (location != null) {
                    onResult(location.latitude, location.longitude)
                } else {

                    // 2️⃣ Fallback: request location update (INI YANG FIX NULL)
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