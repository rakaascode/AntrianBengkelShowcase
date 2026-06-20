package dev.inteiintel.teduhserviceapp.repository

import dev.inteiintel.teduhserviceapp.data.model.Branch
import dev.inteiintel.teduhserviceapp.data.model.DetailBranchResponse
import dev.inteiintel.teduhserviceapp.data.remote.ApiServices
import dev.inteiintel.teduhserviceapp.data.repository.DetailBranchRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DetailBranchRepositoryTest {

    private lateinit var apiServices: ApiServices
    private lateinit var repository: DetailBranchRepository

    private val dummyBranch = Branch(
        id = 3,
        nama = "Cabang Barat",
        alamat = "Jl. Kebon Jeruk No.10",
        kota = "Jakarta Barat",
        no_telp = "021-5551234",
        latitude = -6.1944,
        longitude = 106.7596,
        createdAt = "2024-03-01T00:00:00Z",
        updatedAt = "2024-06-01T00:00:00Z"
    )

    @Before
    fun setUp() {
        apiServices = mockk()
        repository = DetailBranchRepository(apiServices)
    }

    // ─── getBranchById ───────────────────────────────────────────────────────────

    @Test
    fun `getBranchById - sukses mengembalikan Branch`() = runTest {
        // Given
        val response = DetailBranchResponse(success = true, message = "OK", data = dummyBranch)
        coEvery { apiServices.getById(3) } returns response

        // When
        val result = repository.getBranchById(3)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(dummyBranch, result.getOrNull())
    }

    @Test
    fun `getBranchById - data cabang memiliki field yang benar`() = runTest {
        // Given
        coEvery { apiServices.getById(3) } returns DetailBranchResponse(true, "OK", dummyBranch)

        // When
        val result = repository.getBranchById(3)
        val branch = result.getOrNull()

        // Then
        assertEquals(3, branch?.id)
        assertEquals("Cabang Barat", branch?.nama)
        assertEquals("Jakarta Barat", branch?.kota)
        assertEquals("Jl. Kebon Jeruk No.10", branch?.alamat)
    }

    @Test
    fun `getBranchById - latitude dan longitude dikembalikan dengan benar`() = runTest {
        // Given
        coEvery { apiServices.getById(3) } returns DetailBranchResponse(true, "OK", dummyBranch)

        // When
        val result = repository.getBranchById(3)

        // Then
        assertEquals(-6.1944, result.getOrNull()?.latitude)
        assertEquals(106.7596, result.getOrNull()?.longitude)
    }

    @Test
    fun `getBranchById - response success false mengembalikan Result failure`() = runTest {
        // Given
        val failResponse = DetailBranchResponse(success = false, message = "Tidak ditemukan", data = dummyBranch)
        coEvery { apiServices.getById(999) } returns failResponse

        // When
        val result = repository.getBranchById(999)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("999") == true)
    }

    @Test
    fun `getBranchById - exception mengembalikan Result failure`() = runTest {
        // Given
        coEvery { apiServices.getById(any()) } throws Exception("Server tidak tersedia")

        // When
        val result = repository.getBranchById(1)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Server tidak tersedia", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getBranchById - dipanggil dengan branchId yang tepat`() = runTest {
        // Given
        coEvery { apiServices.getById(3) } returns DetailBranchResponse(true, "OK", dummyBranch)

        // When
        repository.getBranchById(3)

        // Then
        coVerify(exactly = 1) { apiServices.getById(3) }
    }

    @Test
    fun `getBranchById - cabang dengan koordinat null tetap bisa dikembalikan`() = runTest {
        // Given
        val branchNullCoord = dummyBranch.copy(latitude = null, longitude = null)
        coEvery { apiServices.getById(5) } returns DetailBranchResponse(true, "OK", branchNullCoord)

        // When
        val result = repository.getBranchById(5)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(null, result.getOrNull()?.latitude)
        assertEquals(null, result.getOrNull()?.longitude)
    }
}
