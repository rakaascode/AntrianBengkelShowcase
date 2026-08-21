package dev.inteiintel.teduhserviceapp.repository

import android.util.Log
import dev.inteiintel.teduhserviceapp.data.model.AntreanActiveData
import dev.inteiintel.teduhserviceapp.data.model.AntreanActiveModelResponse
import dev.inteiintel.teduhserviceapp.data.model.MessageResponse
import dev.inteiintel.teduhserviceapp.data.remote.ApiServices
import dev.inteiintel.teduhserviceapp.data.repository.AntreanActiveRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class AntreanActiveRepositoryTest {

    private lateinit var apiServices: ApiServices
    private lateinit var repository: AntreanActiveRepository

    private val dummyAntreanData = AntreanActiveData(
        id = 10,
        cabang_id = 1,
        user_id = 5,
        nomor_antrian = 3,
        status = "menunggu",
        nama_pemilik = "Andi",
        no_hp = "081234567890",
        merk_motor = "Honda",
        tipe_motor = "Beat",
        no_rangka = "MH1JFZ123",
        no_mesin = "JFZ123",
        tahun_pembuatan = 2020,
        tanggal_kedatangan = "2025-05-20",
        estimasi_jam = "09:00",
        reminder_aktif = false,
        no_wa_reminder = null,
        created_at = "2025-05-19T10:00:00Z"
    )

    @Before
    fun setUp() {
        // Mock android.util.Log agar tidak crash di JVM
        // (repository memanggil Log.d di catch block)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0

        apiServices = mockk()
        repository = AntreanActiveRepository(apiServices)
    }

    // ─── getAntreanActive ────────────────────────────────────────────────────────

    @Test
    fun `getAntreanActive - sukses mengembalikan list antrean aktif`() = runTest {
        // Given
        val response = AntreanActiveModelResponse(data = listOf(dummyAntreanData))
        coEvery { apiServices.getAntreanActive() } returns response

        // When
        val result = repository.getAntreanActive()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertEquals("Andi", result.getOrNull()?.first()?.nama_pemilik)
    }

    @Test
    fun `getAntreanActive - list kosong dikembalikan jika tidak ada antrean`() = runTest {
        // Given
        coEvery { apiServices.getAntreanActive() } returns AntreanActiveModelResponse(data = emptyList())

        // When
        val result = repository.getAntreanActive()

        // Then
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun `getAntreanActive - exception mengembalikan Result failure`() = runTest {
        // Given
        coEvery { apiServices.getAntreanActive() } throws Exception("Server error")

        // When
        val result = repository.getAntreanActive()

        // Then
        assertTrue(result.isFailure)
        assertEquals("Server error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getAntreanActive - data antrean memiliki field yang benar`() = runTest {
        // Given
        coEvery { apiServices.getAntreanActive() } returns AntreanActiveModelResponse(data = listOf(dummyAntreanData))

        // When
        val result = repository.getAntreanActive()
        val item = result.getOrNull()?.first()

        // Then
        assertEquals(10, item?.id)
        assertEquals("Honda", item?.merk_motor)
        assertEquals("Beat", item?.tipe_motor)
        assertEquals("menunggu", item?.status)
    }

    // ─── batalAntrean ────────────────────────────────────────────────────────────

    @Test
    fun `batalAntrean - sukses mengembalikan pesan berhasil`() = runTest {
        // Given
        val antreanId = 10
        val successBody = MessageResponse(success = true, message = "Antrean dibatalkan")
        coEvery { apiServices.batalAntrean(antreanId) } returns Response.success(successBody)

        // When
        val result = repository.batalAntrean(antreanId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals("Antrean dibatalkan", result.getOrNull())
    }

    @Test
    fun `batalAntrean - body null mengembalikan pesan default Berhasil`() = runTest {
        // Given
        val antreanId = 10
        coEvery { apiServices.batalAntrean(antreanId) } returns Response.success(null)

        // When
        val result = repository.batalAntrean(antreanId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals("Berhasil", result.getOrNull())
    }

    @Test
    fun `batalAntrean - HTTP error 404 mengembalikan Result failure`() = runTest {
        // Given
        val antreanId = 99
        coEvery { apiServices.batalAntrean(antreanId) } returns
                Response.error(404, "Not Found".toResponseBody())

        // When
        val result = repository.batalAntrean(antreanId)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `batalAntrean - exception mengembalikan Result failure`() = runTest {
        // Given
        val antreanId = 10
        coEvery { apiServices.batalAntrean(antreanId) } throws Exception("Timeout")

        // When
        val result = repository.batalAntrean(antreanId)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Timeout", result.exceptionOrNull()?.message)
    }

    @Test
    fun `batalAntrean - dipanggil dengan ID yang benar`() = runTest {
        // Given
        val antreanId = 42
        coEvery { apiServices.batalAntrean(antreanId) } returns
                Response.success(MessageResponse(success = true, message = "OK"))

        // When
        repository.batalAntrean(antreanId)

        // Then
        coVerify(exactly = 1) { apiServices.batalAntrean(42) }
    }
}
