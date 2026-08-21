package dev.inteiintel.teduhserviceapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class CreateAntrianResponse(
    val success: Boolean,
    val message: String,
    val data: AntrianData
) : Parcelable

@Parcelize
data class AntrianData(
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


@Parcelize
data class CreateAntrianRequest(
    val cabang_id: Int,
    val nama_pemilik: String,
    val no_polisi: String,
    val merk_motor: String,
    val tipe_motor: String,
    val no_rangka: String,
    val no_mesin: String,
    val tahun_pembuatan: Int,
    val tanggal_kedatangan: String,
    val estimasi_jam: String,
    val catatan: String? = null,
    val reminder_aktif: Boolean = false,
    val no_wa_reminder: String? = null
) : Parcelable

data class Antrian(
    val id: Int,
    val nomor_antrian: Int,
    val status: String,
    val tanggal_kedatangan: String,
    val estimasi_jam: String,
    val merk_motor: String,
    val tipe_motor: String,
    val created_at: String,
    val cabang: Cabang
)


@Parcelize
data class AntrianFormData(
    val nama_pemilik: String,
    val no_polisi: String,
    val merk_motor: String,
    val tipe_motor: String,
    val no_rangka: String,
    val no_mesin: String,
    val tahun_pembuatan: Int,
    val tanggal_kedatangan: String,
    val estimasi_jam: String,
    val catatan: String? = null,
    val reminder_aktif: Boolean = false,
    val no_wa_reminder: String? = null
) : Parcelable