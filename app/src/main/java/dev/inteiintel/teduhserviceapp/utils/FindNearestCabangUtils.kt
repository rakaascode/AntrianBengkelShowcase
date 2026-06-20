package dev.inteiintel.teduhserviceapp.utils

import android.location.Location
import dev.inteiintel.teduhserviceapp.data.model.Branch

/**
 * Utilitas statis untuk perhitungan geospasial terkait cabang bengkel.
 *
 * Digunakan oleh fitur pencarian cabang terdekat pada layar [HomeScreen]
 * dan [DaftarCabangScreen].
 */
object FindUtils {

    /**
     * Menghitung jarak antara posisi pengguna dan posisi cabang dalam kilometer.
     *
     * Menggunakan [android.location.Location.distanceBetween] yang mengimplementasikan
     * formula Vincenty untuk akurasi tinggi di permukaan bumi.
     *
     * @param userLat Latitude posisi pengguna saat ini.
     * @param userLng Longitude posisi pengguna saat ini.
     * @param branchLat Latitude lokasi cabang bengkel.
     * @param branchLng Longitude lokasi cabang bengkel.
     * @return Jarak dalam satuan kilometer (km).
     */
    fun calculateDistanceKm(
        userLat: Double,
        userLng: Double,
        branchLat: Double,
        branchLng: Double
    ): Double {

        val results = FloatArray(1)

        android.location.Location.distanceBetween(
            userLat,
            userLng,
            branchLat,
            branchLng,
            results
        )

        return results[0] / 1000.0 // KM
    }
}