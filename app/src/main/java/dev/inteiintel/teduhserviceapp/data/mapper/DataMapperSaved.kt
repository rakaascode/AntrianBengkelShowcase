package dev.inteiintel.teduhserviceapp.data.mapper


import dev.inteiintel.teduhserviceapp.data.local.room.SavedAntrianEntity
import dev.inteiintel.teduhserviceapp.data.model.CreateAntrianRequest

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