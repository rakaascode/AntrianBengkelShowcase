package dev.inteiintel.teduhserviceapp.presentation.main.home.ambil_antrean.tambah_data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.inteiintel.teduhserviceapp.data.local.room.SavedAntrianEntity
import dev.inteiintel.teduhserviceapp.data.model.CreateAntrianRequest
import dev.inteiintel.teduhserviceapp.data.repository.SavedAntrianRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedAntrianViewModel @Inject constructor(
    private val repository: SavedAntrianRepository
) : ViewModel() {



    fun saveToLocal(request: CreateAntrianRequest) {
        viewModelScope.launch {

            val entity = SavedAntrianEntity(
                cabang_id = request.cabang_id,
                nama_pemilik = request.nama_pemilik,
                no_polisi = request.no_polisi,
                merk_motor = request.merk_motor,
                tipe_motor = request.tipe_motor,
                no_rangka = request.no_rangka,
                no_mesin = request.no_mesin,
                tahun_pembuatan = request.tahun_pembuatan,
                tanggal_kedatangan = request.tanggal_kedatangan,
                estimasi_jam = request.estimasi_jam,
                catatan = request.catatan,
                reminder_aktif = request.reminder_aktif,
                no_wa_reminder = request.no_wa_reminder
            )

            repository.save(entity)
        }
    }
}
