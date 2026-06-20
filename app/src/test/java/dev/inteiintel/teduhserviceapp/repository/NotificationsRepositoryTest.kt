package dev.inteiintel.teduhserviceapp.repository

import android.util.Log
import dev.inteiintel.teduhserviceapp.data.model.NotificationData
import dev.inteiintel.teduhserviceapp.data.model.NotificationResponse
import dev.inteiintel.teduhserviceapp.data.remote.ApiServices
import dev.inteiintel.teduhserviceapp.data.repository.NotificationsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NotificationsRepositoryTest {

    private lateinit var apiServices: ApiServices
    private lateinit var repository: NotificationsRepository

    private val dummyNotif1 = NotificationData(
        id = 1,
        judul = "Promo Ganti Oli",
        deskripsi = "Diskon 20% untuk ganti oli",
        tipe = "promo",
        cabang_id = "1",
        created_at = "2025-05-10T08:00:00Z",
        isRead = false
    )

    private val dummyNotif2 = NotificationData(
        id = 2,
        judul = "Antrian Dipanggil",
        deskripsi = "Nomor antrian Anda sudah dipanggil",
        tipe = "antrian",
        cabang_id = null,
        created_at = "2025-05-15T10:30:00Z",
        isRead = true
    )

    @Before
    fun setUp() {
        // Mock android.util.Log agar tidak crash di JVM
        // (repository memanggil Log.d di catch block)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0

        apiServices = mockk()
        repository = NotificationsRepository(apiServices)
    }

    // ─── getAllNotifications ─────────────────────────────────────────────────────

    @Test
    fun `getAllNotifications - sukses mengembalikan list notifikasi`() = runTest {
        // Given
        val response = NotificationResponse(success = true, data = listOf(dummyNotif1, dummyNotif2))
        coEvery { apiServices.getAllNotifications() } returns response

        // When
        val result = repository.getAllNotifications()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
    }

    @Test
    fun `getAllNotifications - notifikasi pertama memiliki judul yang benar`() = runTest {
        // Given
        coEvery { apiServices.getAllNotifications() } returns
                NotificationResponse(success = true, data = listOf(dummyNotif1))

        // When
        val result = repository.getAllNotifications()

        // Then
        assertEquals("Promo Ganti Oli", result.getOrNull()?.first()?.judul)
        assertEquals("promo", result.getOrNull()?.first()?.tipe)
    }

    @Test
    fun `getAllNotifications - list kosong saat tidak ada notifikasi`() = runTest {
        // Given
        coEvery { apiServices.getAllNotifications() } returns NotificationResponse(success = true, data = emptyList())

        // When
        val result = repository.getAllNotifications()

        // Then
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun `getAllNotifications - notifikasi dengan cabang_id null tetap dikembalikan`() = runTest {
        // Given
        coEvery { apiServices.getAllNotifications() } returns
                NotificationResponse(success = true, data = listOf(dummyNotif2))

        // When
        val result = repository.getAllNotifications()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(null, result.getOrNull()?.first()?.cabang_id)
        assertTrue(result.getOrNull()?.first()?.isRead == true)
    }

    @Test
    fun `getAllNotifications - exception mengembalikan Result failure`() = runTest {
        // Given
        coEvery { apiServices.getAllNotifications() } throws Exception("Gagal mengambil notifikasi")

        // When
        val result = repository.getAllNotifications()

        // Then
        assertTrue(result.isFailure)
        assertEquals("Gagal mengambil notifikasi", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getAllNotifications - API dipanggil tepat sekali`() = runTest {
        // Given
        coEvery { apiServices.getAllNotifications() } returns NotificationResponse(true, emptyList())

        // When
        repository.getAllNotifications()

        // Then
        coVerify(exactly = 1) { apiServices.getAllNotifications() }
    }
}
