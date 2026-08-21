package dev.inteiintel.teduhserviceapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AntreanActiveModelResponse(
    val success: Boolean,
    val data: List<AntreanActiveData>
): Parcelable

@Parcelize
data class AntreanActiveData(
    val id: Int,
    val cabang_id: Int,
    val user_id: Int,
    val nomor_antrian: Int,
    val status: String,
    val nama_pemilik: String,
    val no_polisi: String,
    val merk_motor: String,
    val tipe_motor: String,
    val no_rangka: String,
    val no_mesin: String,
    val tahun_pembuatan: Int,
    val tanggal_kedatangan: String,
    val estimasi_jam: String,
    val reminder_aktif: Boolean,
    val no_wa_reminder: String?,
    val created_at: String
) : Parcelable
