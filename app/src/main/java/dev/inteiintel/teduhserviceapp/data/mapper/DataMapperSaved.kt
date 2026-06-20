package dev.inteiintel.teduhserviceapp.data.mapper


import dev.inteiintel.teduhserviceapp.data.local.room.SavedAntrianEntity
import dev.inteiintel.teduhserviceapp.data.model.CreateAntrianRequest

/**
 * Mengkonversi [SavedAntrianEntity] (data lokal) menjadi [CreateAntrianRequest] (payload API).
 *
 * Digunakan oleh [NavGraph] saat pengguna memilih kendaraan tersimpan di [PilihDataKendaraanScreen],
 * agar data lokal dapat langsung diteruskan ke alur pembuatan antrian tanpa re-input manual.
 *
 * @receiver Entitas antrian lokal dari Room database.
 * @return [CreateAntrianRequest] siap dikirim ke endpoint `POST /antrian`.
 *
 * @see dev.inteiintel.teduhserviceapp.data.local.room.SavedAntrianEntity
 * @see dev.inteiintel.teduhserviceapp.data.model.CreateAntrianRequest
 */
fun SavedAntrianEntity.toRequest(): CreateAntrianRequest {
    return CreateAntrianRequest(
        cabang_id = cabang_id,
        nama_pemilik = nama_pemilik,
        no_hp = no_hp,
        merk_motor = merk_motor,
        tipe_motor = tipe_motor,
        no_rangka = no_rangka,
        no_mesin = no_mesin,
        tahun_pembuatan = tahun_pembuatan,
        tanggal_kedatangan = tanggal_kedatangan,
        estimasi_jam = estimasi_jam,
        catatan = catatan,
        reminder_aktif = reminder_aktif,
        no_wa_reminder = no_wa_reminder
    )
}