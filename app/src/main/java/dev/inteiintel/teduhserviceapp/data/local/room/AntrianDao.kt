package dev.inteiintel.teduhserviceapp.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AntrianDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: SavedAntrianEntity)

    @Query("SELECT * FROM saved_antrian ORDER BY id DESC")
    suspend fun getAll(): List<SavedAntrianEntity>

    @Query("DELETE FROM saved_antrian")
    suspend fun clear()
}