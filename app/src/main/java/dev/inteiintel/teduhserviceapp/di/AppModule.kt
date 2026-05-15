package dev.inteiintel.teduhserviceapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.inteiintel.teduhserviceapp.data.local.room.AntrianDao
import dev.inteiintel.teduhserviceapp.data.local.room.AppDatabase
import dev.inteiintel.teduhserviceapp.data.remote.ApiClient
import dev.inteiintel.teduhserviceapp.data.remote.ApiServices
import dev.inteiintel.teduhserviceapp.data.repository.AmbilAntreanRepository
import dev.inteiintel.teduhserviceapp.data.repository.AntreanActiveRepository
import dev.inteiintel.teduhserviceapp.data.repository.UserRepository
import dev.inteiintel.teduhserviceapp.data.repository.AuthRepository
import dev.inteiintel.teduhserviceapp.data.repository.BranchRepository
import dev.inteiintel.teduhserviceapp.data.repository.NotificationsRepository
import dev.inteiintel.teduhserviceapp.data.repository.RingkasanHomeRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(
        @ApplicationContext context: Context
    ): ApiServices {
        return ApiClient.create(context)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        api: ApiServices
    ): UserRepository {
        return UserRepository(api)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        api: ApiServices
    ): AuthRepository {
        return AuthRepository(api)
    }

    @Provides
    @Singleton
    fun provideBranchRepository(
        api: ApiServices
    ): BranchRepository{
        return BranchRepository(api)
    }

    @Provides
    @Singleton
    fun provideAmbilAntreanRepository(
        api: ApiServices
    ): AmbilAntreanRepository{
        return AmbilAntreanRepository(api)
    }

    @Provides
    @Singleton
    fun provideAntreanActiveRepository(
        api: ApiServices
    ): AntreanActiveRepository {
        return AntreanActiveRepository(api)
    }

    @Provides
    @Singleton
    fun provideNotificationsRepository(
        api: ApiServices
    ): NotificationsRepository {
        return NotificationsRepository(api)
    }

    @Provides
    @Singleton
    fun provideRingkasanHomeRepository(
        api: ApiServices
    ): RingkasanHomeRepository {
        return RingkasanHomeRepository(api)
    }


    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "teduh_db"
        ).build()
    }

    @Provides
    fun provideDao(db: AppDatabase): AntrianDao {
        return db.antrianDao()
    }
}