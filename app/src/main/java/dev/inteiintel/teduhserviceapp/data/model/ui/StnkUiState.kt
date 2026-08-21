package dev.inteiintel.teduhserviceapp.data.model.ui

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class StnkUiState(
    val bitmap: Bitmap? = null,
    val isLoading: Boolean = false,
    val result: StnkResult? = null
)


@Parcelize
data class StnkResult(
    val nama: String? = null,
    val no_polisi: String? = null,
    val alamat: String? = null,
    val no_rangka: String? = null,
    val no_mesin: String? = null,
    val tipe: String? = null,
    val tahun: String? = null
): Parcelable
