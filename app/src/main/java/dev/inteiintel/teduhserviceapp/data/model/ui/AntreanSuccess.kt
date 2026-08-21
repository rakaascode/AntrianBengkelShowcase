package dev.inteiintel.teduhserviceapp.data.model.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AntreanSuccess(
    val tipeMotor: String,
    val noPolisi: String,
    val tanggalKedatangan: String,
    val jamKedatangan: String,
    val branchId: Int
) : Parcelable