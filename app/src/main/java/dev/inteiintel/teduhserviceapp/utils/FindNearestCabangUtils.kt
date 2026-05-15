package dev.inteiintel.teduhserviceapp.utils

import android.location.Location
import dev.inteiintel.teduhserviceapp.data.model.Branch

object FindUtils {

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

    fun sortByNearest(
        userLat: Double,
        userLng: Double,
        cabangList: List<Branch>
    ): List<Branch> {

        return cabangList
            .filter { it.latitude != null && it.longitude != null }
            .map { branch ->
                branch to calculateDistanceKm(
                    userLat,
                    userLng,
                    branch.latitude!!,
                    branch.longitude!!
                )
            }
            .sortedBy { it.second }
            .map { it.first }
    }
}