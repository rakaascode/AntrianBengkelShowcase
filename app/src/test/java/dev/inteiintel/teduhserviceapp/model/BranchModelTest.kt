package dev.inteiintel.teduhserviceapp.model

import dev.inteiintel.teduhserviceapp.data.model.Branch
import dev.inteiintel.teduhserviceapp.data.model.BranchResponse
import dev.inteiintel.teduhserviceapp.data.model.BranchWithDistance
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BranchModelTest {

    private val sampleBranch = Branch(
        id = 1,
        nama = "Cabang Pusat",
        alamat = "Jl. Merdeka No.1",
        kota = "Jakarta",
        no_telp = "021-0000001",
        latitude = -6.2088,
        longitude = 106.8456,
        createdAt = "2024-01-01T00:00:00Z",
        updatedAt = "2024-06-01T00:00:00Z"
    )

    // ─── Branch model ────────────────────────────────────────────────────────────

    @Test
    fun `Branch - property dasar dapat diakses dengan benar`() {
        assertEquals(1, sampleBranch.id)
        assertEquals("Cabang Pusat", sampleBranch.nama)
        assertEquals("Jakarta", sampleBranch.kota)
        assertEquals("021-0000001", sampleBranch.no_telp)
    }

    @Test
    fun `Branch - koordinat lat dan lng tersedia`() {
        assertEquals(-6.2088, sampleBranch.latitude)
        assertEquals(106.8456, sampleBranch.longitude)
    }

    @Test
    fun `Branch - koordinat bisa bernilai null`() {
        val branchNoCoord = sampleBranch.copy(latitude = null, longitude = null)
        assertNull(branchNoCoord.latitude)
        assertNull(branchNoCoord.longitude)
    }

    @Test
    fun `Branch - copy menghasilkan objek baru yang tidak sama referensinya`() {
        val copy = sampleBranch.copy(nama = "Cabang Lain")
        assertEquals("Cabang Lain", copy.nama)
        assertFalse(copy === sampleBranch)
    }

    @Test
    fun `Branch - dua objek dengan data sama dianggap equal`() {
        val branch2 = sampleBranch.copy()
        assertEquals(sampleBranch, branch2)
    }

    @Test
    fun `Branch - dua objek dengan data berbeda tidak equal`() {
        val branch2 = sampleBranch.copy(id = 2, nama = "Cabang Lain")
        assertFalse(sampleBranch == branch2)
    }

    // ─── BranchResponse model ────────────────────────────────────────────────────

    @Test
    fun `BranchResponse - success true dan data tidak kosong`() {
        val response = BranchResponse(success = true, message = "OK", data = listOf(sampleBranch))
        assertTrue(response.success)
        assertEquals(1, response.data.size)
    }

    @Test
    fun `BranchResponse - success false dengan pesan error`() {
        val response = BranchResponse(success = false, message = "Server Error", data = emptyList())
        assertFalse(response.success)
        assertEquals("Server Error", response.message)
        assertTrue(response.data.isEmpty())
    }

    @Test
    fun `BranchResponse - data bisa berisi banyak cabang`() {
        val branches = List(10) { i -> sampleBranch.copy(id = i + 1, nama = "Cabang ${i + 1}") }
        val response = BranchResponse(success = true, message = "OK", data = branches)
        assertEquals(10, response.data.size)
        assertEquals("Cabang 5", response.data[4].nama)
    }

    // ─── BranchWithDistance model ─────────────────────────────────────────────────

    @Test
    fun `BranchWithDistance - menyimpan cabang dan jarak dengan benar`() {
        val bwd = BranchWithDistance(branch = sampleBranch, distanceKm = 3.75)
        assertEquals(sampleBranch, bwd.branch)
        assertEquals(3.75, bwd.distanceKm, 0.001)
    }

    @Test
    fun `BranchWithDistance - jarak nol untuk lokasi yang sama`() {
        val bwd = BranchWithDistance(branch = sampleBranch, distanceKm = 0.0)
        assertEquals(0.0, bwd.distanceKm, 0.0)
    }

    @Test
    fun `BranchWithDistance - sorting berdasarkan jarak berfungsi`() {
        val near  = BranchWithDistance(sampleBranch.copy(id = 1), 1.2)
        val mid   = BranchWithDistance(sampleBranch.copy(id = 2), 5.0)
        val far   = BranchWithDistance(sampleBranch.copy(id = 3), 20.5)
        val list  = listOf(far, near, mid).sortedBy { it.distanceKm }

        assertEquals(1.2,  list[0].distanceKm, 0.001)
        assertEquals(5.0,  list[1].distanceKm, 0.001)
        assertEquals(20.5, list[2].distanceKm, 0.001)
    }
}
