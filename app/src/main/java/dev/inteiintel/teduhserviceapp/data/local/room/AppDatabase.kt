package dev.inteiintel.teduhserviceapp.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [SavedAntrianEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun antrianDao(): AntrianDao
}