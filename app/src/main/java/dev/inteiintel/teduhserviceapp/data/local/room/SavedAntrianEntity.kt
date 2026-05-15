package dev.inteiintel.teduhserviceapp.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_antrian")
data class SavedAntrianEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cabang_id: Int,
    val nama_pemilik: String,
    val no_hp: String,
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
)
