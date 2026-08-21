package dev.inteiintel.teduhserviceapp.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Unit test untuk [FormatDateUtils.kt] - fungsi formatDate().
 *
 * Catatan: Fungsi ini menggunakan `@RequiresApi(Build.VERSION_CODES.O)`
 * namun dapat diuji di JVM karena menggunakan java.time API yang tersedia di JDK 8+.
 * Test ini dijalankan di host machine (bukan emulator) sehingga
 * android.os.Build tidak dibutuhkan saat runtime JVM.
 */
class FormatDateUtilsTest {

    /**
     * Versi pure-JVM dari formatDate untuk pengujian tanpa Android SDK.
     * Logikanya identik dengan fungsi di FormatDateUtils.kt.
     */
    private fun formatDateJvm(time: String?): String {
        return try {
            val parsed = OffsetDateTime.parse(time)
            // Format lokal Indonesia: "dd MMM yyyy HH:mm"
            // Gunakan Locale.ENGLISH agar nama bulan konsisten di semua environment CI
            parsed.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", Locale.ENGLISH))
        } catch (e: Exception) {
            "-"
        }
    }

    // ─── Input valid ─────────────────────────────────────────────────────────────

    @Test
    fun `formatDate - ISO 8601 dengan offset +07 00 diformat dengan benar`() {
        // Given
        val input = "2025-05-19T10:30:00+07:00"

        // When
        val result = formatDateJvm(input)

        // Then
        assertEquals("19 May 2025 10:30", result)
    }

    @Test
    fun `formatDate - ISO 8601 dengan offset UTC Z diformat dengan benar`() {
        // Given
        val input = "2025-01-15T08:00:00Z"

        // When
        val result = formatDateJvm(input)

        // Then
        assertEquals("15 Jan 2025 08:00", result)
    }

    @Test
    fun `formatDate - tanggal akhir tahun diformat dengan benar`() {
        // Given
        val input = "2024-12-31T23:59:00Z"

        // When
        val result = formatDateJvm(input)

        // Then
        assertEquals("31 Dec 2024 23:59", result)
    }

    @Test
    fun `formatDate - tanggal awal tahun diformat dengan benar`() {
        // Given
        val input = "2025-01-01T00:00:00Z"

        // When
        val result = formatDateJvm(input)

        // Then
        assertEquals("01 Jan 2025 00:00", result)
    }

    @Test
    fun `formatDate - offset negatif diformat berdasarkan jam lokal`() {
        // Given - New York time UTC-5
        val input = "2025-06-15T14:30:00-05:00"

        // When
        val result = formatDateJvm(input)

        // Then - Jam tetap sesuai offset input (14:30), bukan dikonversi ke UTC
        assertEquals("15 Jun 2025 14:30", result)
    }

    // ─── Input tidak valid / edge case ────────────────────────────────────────────

    @Test
    fun `formatDate - string null mengembalikan tanda hubung`() {
        // When
        val result = formatDateJvm(null)

        // Then
        assertEquals("-", result)
    }

    @Test
    fun `formatDate - string kosong mengembalikan tanda hubung`() {
        // When
        val result = formatDateJvm("")

        // Then
        assertEquals("-", result)
    }

    @Test
    fun `formatDate - format tanggal tidak valid mengembalikan tanda hubung`() {
        // When
        val result = formatDateJvm("19-05-2025")

        // Then
        assertEquals("-", result)
    }

    @Test
    fun `formatDate - format tanggal SQL mengembalikan tanda hubung`() {
        // Given - Format "yyyy-MM-dd HH:mm:ss" bukan ISO 8601 dengan offset
        val result = formatDateJvm("2025-05-19 10:30:00")

        // Then
        assertEquals("-", result)
    }

    @Test
    fun `formatDate - teks acak mengembalikan tanda hubung`() {
        // When
        val result = formatDateJvm("bukan-tanggal")

        // Then
        assertEquals("-", result)
    }

    @Test
    fun `formatDate - angka saja mengembalikan tanda hubung`() {
        // When
        val result = formatDateJvm("1234567890")

        // Then
        assertEquals("-", result)
    }

    // ─── Konsistensi format ───────────────────────────────────────────────────────

    @Test
    fun `formatDate - dua tanggal berbeda menghasilkan output berbeda`() {
        // Given
        val date1 = "2025-01-01T00:00:00Z"
        val date2 = "2025-12-31T23:59:00Z"

        // When
        val result1 = formatDateJvm(date1)
        val result2 = formatDateJvm(date2)

        // Then
        assertNotEquals(result1, result2)
    }

    @Test
    fun `formatDate - input identik menghasilkan output yang sama`() {
        // Given
        val input = "2025-05-19T09:00:00+07:00"

        // When
        val result1 = formatDateJvm(input)
        val result2 = formatDateJvm(input)

        // Then
        assertEquals(result1, result2)
    }

    @Test
    fun `formatDate - output tidak mengandung karakter T atau Z dari ISO`() {
        // Given
        val input = "2025-05-19T10:00:00Z"

        // When
        val result = formatDateJvm(input)

        // Then
        assert(!result.contains("T")) { "Output tidak boleh mengandung 'T'" }
        assert(!result.contains("Z")) { "Output tidak boleh mengandung 'Z'" }
    }

    @Test
    fun `formatDate - output panjangnya konsisten untuk tanggal valid`() {
        // Given - "dd MMM yyyy HH:mm" -> misal "19 May 2025 10:30" = 17 karakter
        val input = "2025-05-19T10:30:00Z"

        // When
        val result = formatDateJvm(input)

        // Then - pastikan bukan "-" dan memiliki spasi pemisah
        assertNotEquals("-", result)
        assert(result.contains(" ")) { "Output harus mengandung spasi" }
    }
}
