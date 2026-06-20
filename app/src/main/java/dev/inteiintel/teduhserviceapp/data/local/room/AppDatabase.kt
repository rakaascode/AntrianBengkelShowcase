package dev.inteiintel.teduhserviceapp.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Database Room utama aplikasi Teduh Service.
 *
 * Menyimpan data antrian lokal yang belum dikirim ke server atau disimpan sebagai draft
 * oleh pengguna. Versi database: 1.
 *
 * Disediakan sebagai singleton melalui [AppModule.provideDatabase].
 *
 * @see dev.inteiintel.teduhserviceapp.data.local.room.AntrianDao
 * @see dev.inteiintel.teduhserviceapp.data.local.room.SavedAntrianEntity
 * @see dev.inteiintel.teduhserviceapp.di.AppModule
 */
@Database(
    entities = [SavedAntrianEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    /** Mengembalikan DAO untuk operasi CRUD pada tabel antrian tersimpan. */
    abstract fun antrianDao(): AntrianDao
}