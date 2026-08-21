package dev.inteiintel.teduhserviceapp.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity Room yang merepresentasikan satu data antrian tersimpan secara lokal.
 *
 * Disimpan ke tabel `saved_antrian` oleh pengguna melalui layar [TambahDataScreen]
 * sehingga dapat dipilih kembali di layar [DataTersimpanScreen] tanpa harus mengisi
 * ulang formulir.
 *
 * Dapat dikonversi ke [CreateAntrianRequest] menggunakan ekstensi [toRequest].
 *
 * @see dev.inteiintel.teduhserviceapp.data.local.room.AntrianDao
 * @see dev.inteiintel.teduhserviceapp.data.mapper.DataMapperSaved
 */
@Entity(tableName = "saved_antrian")
data class SavedAntrianEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
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
)
