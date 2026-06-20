package dev.inteiintel.teduhserviceapp.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) untuk operasi CRUD pada tabel `saved_antrian`.
 *
 * Digunakan oleh [SavedAntrianRepository] untuk menyimpan, membaca, dan menghapus
 * data antrian lokal yang tersimpan di perangkat.
 *
 * @see dev.inteiintel.teduhserviceapp.data.local.room.SavedAntrianEntity
 * @see dev.inteiintel.teduhserviceapp.data.repository.SavedAntrianRepository
 */
@Dao
interface AntrianDao {

    /**
     * Menyimpan atau menggantikan satu entitas antrian ke database.
     *
     * Menggunakan strategi [OnConflictStrategy.REPLACE] sehingga data dengan ID sama
     * akan ditimpa.
     *
     * @param data Entitas antrian yang akan disimpan.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: SavedAntrianEntity)

    /**
     * Mengambil semua data antrian tersimpan, diurutkan dari yang terbaru.
     *
     * @return Daftar [SavedAntrianEntity] diurutkan berdasarkan `id` descending.
     */
    @Query("SELECT * FROM saved_antrian ORDER BY id DESC")
    suspend fun getAll(): List<SavedAntrianEntity>

    /** Menghapus semua data antrian tersimpan dari tabel. */
    @Query("DELETE FROM saved_antrian")
    suspend fun clear()
}