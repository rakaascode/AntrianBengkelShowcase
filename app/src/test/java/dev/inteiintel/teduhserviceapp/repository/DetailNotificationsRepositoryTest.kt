package dev.inteiintel.teduhserviceapp.repository

import android.util.Log
import dev.inteiintel.teduhserviceapp.data.model.DetailNotificationData
import dev.inteiintel.teduhserviceapp.data.model.DetailNotificationsResponse
import dev.inteiintel.teduhserviceapp.data.remote.ApiServices
import dev.inteiintel.teduhserviceapp.data.repository.DetailNotificationsRepository
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

class DetailNotificationsRepositoryTest {

    private lateinit var apiServices: ApiServices
    private lateinit var repository: DetailNotificationsRepository

    private val dummyDetailData = DetailNotificationData(
        id = 7,
        judul = "Flash Sale Bengkel",
        deskripsi = "Dapatkan servis gratis untuk motor matik",
        detail = "Detail lengkap promo flash sale selama bulan Mei 2025.",
        gambar_url = "https://example.com/images/flash_sale.jpg",
        tipe = "promo",
        cabang_id = "2",
        created_at = "2025-05-01T08:00:00Z"
    )

    private val dummyResponse = DetailNotificationsResponse(
        success = true,
        data = dummyDetailData
    )

    @Before
    fun setUp() {
        // Mock android.util.Log agar tidak crash di JVM
        // (repository memanggil Log.d di catch block)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0

        apiServices = mockk()
        repository = DetailNotificationsRepository(apiServices)
    }

    // ─── getDetailNotificationById ───────────────────────────────────────────────

    @Test
    fun `getDetailNotificationById - sukses mengembalikan DetailNotificationData`() = runTest {
        // Given
        coEvery { apiServices.getNotificationsById(7) } returns dummyResponse

        // When
        val result = repository.getDetailNotificationById(7)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(dummyDetailData, result.getOrNull())
    }

    @Test
    fun `getDetailNotificationById - data notifikasi memiliki field yang benar`() = runTest {
        // Given
        coEvery { apiServices.getNotificationsById(7) } returns dummyResponse

        // When
        val result = repository.getDetailNotificationById(7)
        val data = result.getOrNull()

        // Then
        assertEquals(7, data?.id)
        assertEquals("Flash Sale Bengkel", data?.judul)
        assertEquals("promo", data?.tipe)
        assertEquals("https://example.com/images/flash_sale.jpg", data?.gambar_url)
    }

    @Test
    fun `getDetailNotificationById - cabang_id bisa bernilai null`() = runTest {
        // Given
        val dataWithNullCabang = dummyDetailData.copy(cabang_id = null)
        coEvery { apiServices.getNotificationsById(7) } returns
                DetailNotificationsResponse(success = true, data = dataWithNullCabang)

        // When
        val result = repository.getDetailNotificationById(7)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(null, result.getOrNull()?.cabang_id)
    }

    @Test
    fun `getDetailNotificationById - exception mengembalikan Result failure`() = runTest {
        // Given
        coEvery { apiServices.getNotificationsById(any()) } throws Exception("Notifikasi tidak ditemukan")

        // When
        val result = repository.getDetailNotificationById(999)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Notifikasi tidak ditemukan", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getDetailNotificationById - dipanggil dengan ID yang benar`() = runTest {
        // Given
        coEvery { apiServices.getNotificationsById(7) } returns dummyResponse

        // When
        repository.getDetailNotificationById(7)

        // Then
        coVerify(exactly = 1) { apiServices.getNotificationsById(7) }
    }

    @Test
    fun `getDetailNotificationById - ID berbeda menghasilkan data berbeda`() = runTest {
        // Given
        val otherData = dummyDetailData.copy(id = 99, judul = "Update Sistem")
        coEvery { apiServices.getNotificationsById(99) } returns
                DetailNotificationsResponse(success = true, data = otherData)

        // When
        val result = repository.getDetailNotificationById(99)

        // Then
        assertEquals(99, result.getOrNull()?.id)
        assertEquals("Update Sistem", result.getOrNull()?.judul)
    }
}
