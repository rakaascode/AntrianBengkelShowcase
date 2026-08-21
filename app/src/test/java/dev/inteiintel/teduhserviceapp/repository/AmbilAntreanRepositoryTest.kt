package dev.inteiintel.teduhserviceapp.repository

import dev.inteiintel.teduhserviceapp.data.model.AntrianData
import dev.inteiintel.teduhserviceapp.data.model.CreateAntrianRequest
import dev.inteiintel.teduhserviceapp.data.model.CreateAntrianResponse
import dev.inteiintel.teduhserviceapp.data.remote.ApiServices
import dev.inteiintel.teduhserviceapp.data.repository.AmbilAntreanRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class AmbilAntreanRepositoryTest {

    private lateinit var apiServices: ApiServices
    private lateinit var repository: AmbilAntreanRepository

    private val dummyRequest = CreateAntrianRequest(
        cabang_id = 1,
        nama_pemilik = "Budi Santoso",
        no_hp = "081234567890",
        merk_motor = "Yamaha",
        tipe_motor = "NMAX",
        no_rangka = "MH3SG1234",
        no_mesin = "SG1234",
        tahun_pembuatan = 2022,
        tanggal_kedatangan = "2025-05-20",
        estimasi_jam = "10:00",
        catatan = "Ganti oli",
        reminder_aktif = true,
        no_wa_reminder = "081234567890"
    )

    private val dummyAntrianData = AntrianData(
        id = 99,
        cabang_id = 1,
        user_id = 3,
        nomor_antrian = 7,
        status = "menunggu",
        nama_pemilik = "Budi Santoso",
        no_hp = "081234567890",
        merk_motor = "Yamaha",
        tipe_motor = "NMAX",
        no_rangka = "MH3SG1234",
        no_mesin = "SG1234",
        tahun_pembuatan = 2022,
        tanggal_kedatangan = "2025-05-20",
        estimasi_jam = "10:00",
        reminder_aktif = true,
        no_wa_reminder = "081234567890",
        created_at = "2025-05-19T10:00:00Z"
    )

    private val dummyResponse = CreateAntrianResponse(
        success = true,
        message = "Antrean berhasil dibuat",
        data = dummyAntrianData
    )

    @Before
    fun setUp() {
        apiServices = mockk()
        repository = AmbilAntreanRepository(apiServices)
    }

    // ─── ambilAntreanUsers ───────────────────────────────────────────────────────

    @Test
    fun `ambilAntreanUsers - sukses mengembalikan CreateAntrianResponse`() = runTest {
        // Given
        coEvery { apiServices.ambilAntreanUsers(dummyRequest) } returns Response.success(dummyResponse)

        // When
        val result = repository.ambilAntreanUsers(dummyRequest)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(dummyResponse, result.getOrNull())
    }

    @Test
    fun `ambilAntreanUsers - nomor antrian dan status dikembalikan dengan benar`() = runTest {
        // Given
        coEvery { apiServices.ambilAntreanUsers(dummyRequest) } returns Response.success(dummyResponse)

        // When
        val result = repository.ambilAntreanUsers(dummyRequest)

        // Then
        assertEquals(7, result.getOrNull()?.data?.nomor_antrian)
        assertEquals("menunggu", result.getOrNull()?.data?.status)
    }

    @Test
    fun `ambilAntreanUsers - body null mengembalikan Result failure`() = runTest {
        // Given
        coEvery { apiServices.ambilAntreanUsers(dummyRequest) } returns Response.success(null)

        // When
        val result = repository.ambilAntreanUsers(dummyRequest)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `ambilAntreanUsers - HTTP 422 Unprocessable Entity mengembalikan Result failure`() = runTest {
        // Given
        coEvery { apiServices.ambilAntreanUsers(dummyRequest) } returns
                Response.error(422, "Unprocessable Entity".toResponseBody())

        // When
        val result = repository.ambilAntreanUsers(dummyRequest)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("422") == true)
    }

    @Test
    fun `ambilAntreanUsers - HTTP 400 Bad Request mengembalikan Result failure`() = runTest {
        // Given
        coEvery { apiServices.ambilAntreanUsers(dummyRequest) } returns
                Response.error(400, "Bad Request".toResponseBody())

        // When
        val result = repository.ambilAntreanUsers(dummyRequest)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("400") == true)
    }

    @Test
    fun `ambilAntreanUsers - exception IOException mengembalikan Result failure dengan pesan`() = runTest {
        // Given
        val exception = Exception("Tidak ada koneksi internet")
        coEvery { apiServices.ambilAntreanUsers(dummyRequest) } throws exception

        // When
        val result = repository.ambilAntreanUsers(dummyRequest)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Tidak ada koneksi internet", result.exceptionOrNull()?.message)
    }

    @Test
    fun `ambilAntreanUsers - API dipanggil dengan request yang benar`() = runTest {
        // Given
        coEvery { apiServices.ambilAntreanUsers(dummyRequest) } returns Response.success(dummyResponse)

        // When
        repository.ambilAntreanUsers(dummyRequest)

        // Then
        coVerify(exactly = 1) { apiServices.ambilAntreanUsers(dummyRequest) }
    }
}
