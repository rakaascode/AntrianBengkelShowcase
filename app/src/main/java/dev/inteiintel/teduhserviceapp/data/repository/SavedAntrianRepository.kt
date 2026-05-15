package dev.inteiintel.teduhserviceapp.data.repository

import dev.inteiintel.teduhserviceapp.data.local.room.AntrianDao
import dev.inteiintel.teduhserviceapp.data.local.room.SavedAntrianEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SavedAntrianRepository @Inject constructor(
    private val dao: AntrianDao
) {
    suspend fun save(data: SavedAntrianEntity) {
        dao.insert(data)
    }

    suspend fun getAll(): List<SavedAntrianEntity> {
        return dao.getAll()
    }
}