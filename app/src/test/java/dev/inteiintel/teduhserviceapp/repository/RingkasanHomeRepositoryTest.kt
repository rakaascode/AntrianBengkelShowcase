package dev.inteiintel.teduhserviceapp.repository

import dev.inteiintel.teduhserviceapp.data.model.RingkasanCabangItem
import dev.inteiintel.teduhserviceapp.data.model.RingkasanHomeResponse
import dev.inteiintel.teduhserviceapp.data.remote.ApiServices
import dev.inteiintel.teduhserviceapp.data.repository.RingkasanHomeRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException

class RingkasanHomeRepositoryTest {

    private lateinit var apiServices: ApiServices
    private lateinit var repository: RingkasanHomeRepository

    private val dummyCabang1 = RingkasanCabangItem(
        cabangId = 1,
        namaCabang = "Cabang Utama",
        latitude = -6.2088,
        longitude = 106.8456,
        nomorDipanggil = 5,
        estimasiJam = "09:30",
        sisaAntrian = 12
    )

    private val dummyCabang2 = RingkasanCabangItem(
        cabangId = 2,
        namaCabang = "Cabang Selatan",
        latitude = -6.9175,
        longitude = 107.6191,
        nomorDipanggil = null,
        estimasiJam = "10:00",
        sisaAntrian = 0
    )

    private val dummyResponse = RingkasanHomeResponse(
        success = true,
        totalCabang = 2,
        data = listOf(dummyCabang1, dummyCabang2)
    )

    @Before
    fun setUp() {
        apiServices = mockk()
        repository = RingkasanHomeRepository(apiServices)
    }

    // ─── getRingkasanHome ────────────────────────────────────────────────────────

    @Test
    fun `getRingkasanHome - sukses mengembalikan RingkasanHomeResponse`() = runTest {
        // Given
        coEvery { apiServices.getAllRingkasan() } returns dummyResponse

        // When
        val result = repository.getRingkasanHome()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(dummyResponse, result.getOrNull())
    }

    @Test
    fun `getRingkasanHome - total cabang dikembalikan dengan benar`() = runTest {
        // Given
        coEvery { apiServices.getAllRingkasan() } returns dummyResponse

        // When
        val result = repository.getRingkasanHome()

        // Then
        assertEquals(2, result.getOrNull()?.totalCabang)
        assertEquals(2, result.getOrNull()?.data?.size)
    }

    @Test
    fun `getRingkasanHome - nomor dipanggil null pada cabang kosong`() = runTest {
        // Given
        coEvery { apiServices.getAllRingkasan() } returns dummyResponse

        // When
        val result = repository.getRingkasanHome()
        val cabangKosong = result.getOrNull()?.data?.find { it.sisaAntrian == 0 }

        // Then
        assertTrue(cabangKosong != null)
        assertEquals(null, cabangKosong?.nomorDipanggil)
    }

    @Test
    fun `getRingkasanHome - sisa antrian cabang pertama bernilai 12`() = runTest {
        // Given
        coEvery { apiServices.getAllRingkasan() } returns dummyResponse

        // When
        val result = repository.getRingkasanHome()

        // Then
        assertEquals(12, result.getOrNull()?.data?.first()?.sisaAntrian)
    }

    @Test
    fun `getRingkasanHome - HttpException mengembalikan Result failure dengan kode HTTP`() = runTest {
        // Given
        val mockResponse = mockk<okhttp3.Response>(relaxed = true)
        val mockRawResponse = mockk<okhttp3.Response>(relaxed = true)
        val httpException = mockk<HttpException>(relaxed = true)

        coEvery { httpException.code() } returns 500
        coEvery { apiServices.getAllRingkasan() } throws httpException

        // When
        val result = repository.getRingkasanHome()

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("500") == true)
    }

    @Test
    fun `getRingkasanHome - IOException mengembalikan pesan koneksi bermasalah`() = runTest {
        // Given
        coEvery { apiServices.getAllRingkasan() } throws IOException("Network unreachable")

        // When
        val result = repository.getRingkasanHome()

        // Then
        assertTrue(result.isFailure)
        assertEquals("Koneksi internet bermasalah", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getRingkasanHome - generic Exception mengembalikan pesan exception`() = runTest {
        // Given
        coEvery { apiServices.getAllRingkasan() } throws Exception("Terjadi kesalahan server")

        // When
        val result = repository.getRingkasanHome()

        // Then
        assertTrue(result.isFailure)
        assertEquals("Terjadi kesalahan server", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getRingkasanHome - API dipanggil tepat sekali`() = runTest {
        // Given
        coEvery { apiServices.getAllRingkasan() } returns dummyResponse

        // When
        repository.getRingkasanHome()

        // Then
        coVerify(exactly = 1) { apiServices.getAllRingkasan() }
    }
}
