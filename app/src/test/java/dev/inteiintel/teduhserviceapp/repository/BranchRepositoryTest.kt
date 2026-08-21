package dev.inteiintel.teduhserviceapp.repository

import dev.inteiintel.teduhserviceapp.data.model.Branch
import dev.inteiintel.teduhserviceapp.data.model.BranchResponse
import dev.inteiintel.teduhserviceapp.data.remote.ApiServices
import dev.inteiintel.teduhserviceapp.data.repository.BranchRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class BranchRepositoryTest {

    private lateinit var apiServices: ApiServices
    private lateinit var repository: BranchRepository

    private val dummyBranch1 = Branch(
        id = 1,
        nama = "Cabang Jakarta",
        alamat = "Jl. Sudirman No.1",
        kota = "Jakarta",
        no_telp = "021-1234567",
        latitude = -6.2088,
        longitude = 106.8456,
        createdAt = "2024-01-01T00:00:00Z",
        updatedAt = "2024-06-01T00:00:00Z"
    )

    private val dummyBranch2 = Branch(
        id = 2,
        nama = "Cabang Bandung",
        alamat = "Jl. Asia Afrika No.2",
        kota = "Bandung",
        no_telp = "022-7654321",
        latitude = -6.9175,
        longitude = 107.6191,
        createdAt = "2024-01-02T00:00:00Z",
        updatedAt = "2024-06-02T00:00:00Z"
    )

    @Before
    fun setUp() {
        apiServices = mockk()
        repository = BranchRepository(apiServices)
    }

    // ─── getBranch ───────────────────────────────────────────────────────────────

    @Test
    fun `getBranch - sukses mengembalikan list branch`() = runTest {
        // Given
        val response = BranchResponse(success = true, message = "OK", data = listOf(dummyBranch1, dummyBranch2))
        coEvery { apiServices.getAllBranch() } returns response

        // When
        val result = repository.getBranch()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
    }

    @Test
    fun `getBranch - response success false mengembalikan Result failure`() = runTest {
        // Given
        val response = BranchResponse(success = false, message = "Data tidak ditemukan", data = emptyList())
        coEvery { apiServices.getAllBranch() } returns response

        // When
        val result = repository.getBranch()

        // Then
        assertTrue(result.isFailure)
        assertEquals("Data tidak ditemukan", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getBranch - exception mengembalikan Result failure`() = runTest {
        // Given
        coEvery { apiServices.getAllBranch() } throws Exception("Koneksi bermasalah")

        // When
        val result = repository.getBranch()

        // Then
        assertTrue(result.isFailure)
        assertEquals("Koneksi bermasalah", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getBranch - list kosong saat tidak ada cabang`() = runTest {
        // Given
        val response = BranchResponse(success = true, message = "Kosong", data = emptyList())
        coEvery { apiServices.getAllBranch() } returns response

        // When
        val result = repository.getBranch()

        // Then
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun `getBranch - data branch pertama memiliki nama yang benar`() = runTest {
        // Given
        val response = BranchResponse(success = true, message = "OK", data = listOf(dummyBranch1))
        coEvery { apiServices.getAllBranch() } returns response

        // When
        val result = repository.getBranch()

        // Then
        assertEquals("Cabang Jakarta", result.getOrNull()?.first()?.nama)
        assertEquals("Jakarta", result.getOrNull()?.first()?.kota)
    }

    @Test
    fun `getBranch - API dipanggil tepat sekali`() = runTest {
        // Given
        coEvery { apiServices.getAllBranch() } returns BranchResponse(true, "OK", emptyList())

        // When
        repository.getBranch()

        // Then
        coVerify(exactly = 1) { apiServices.getAllBranch() }
    }
}
