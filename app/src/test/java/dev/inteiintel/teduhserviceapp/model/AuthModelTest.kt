package dev.inteiintel.teduhserviceapp.model

import dev.inteiintel.teduhserviceapp.data.model.AuthData
import dev.inteiintel.teduhserviceapp.data.model.AuthResponse
import dev.inteiintel.teduhserviceapp.data.model.GoogleLoginRequest
import dev.inteiintel.teduhserviceapp.data.model.User
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthModelTest {

    private val sampleUser = User(
        id = 1,
        name = "Budi Santoso",
        email = "budi@email.com",
        avatar_url = null,
        role = "user"
    )

    private val sampleAuthData = AuthData(
        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9",
        user = sampleUser
    )

    private val sampleAuthResponse = AuthResponse(
        success = true,
        message = "Login berhasil",
        data = sampleAuthData
    )

    // ─── GoogleLoginRequest ───────────────────────────────────────────────────────

    @Test
    fun `GoogleLoginRequest - menyimpan id_token dengan benar`() {
        val request = GoogleLoginRequest("my_google_id_token")
        assertEquals("my_google_id_token", request.id_token)
    }

    @Test
    fun `GoogleLoginRequest - dua objek dengan token sama dianggap equal`() {
        val r1 = GoogleLoginRequest("token_abc")
        val r2 = GoogleLoginRequest("token_abc")
        assertEquals(r1, r2)
    }

    @Test
    fun `GoogleLoginRequest - dua objek dengan token berbeda tidak equal`() {
        val r1 = GoogleLoginRequest("token_abc")
        val r2 = GoogleLoginRequest("token_xyz")
        assertFalse(r1 == r2)
    }

    @Test
    fun `GoogleLoginRequest - token kosong tetap tersimpan`() {
        val request = GoogleLoginRequest("")
        assertEquals("", request.id_token)
    }

    // ─── User ────────────────────────────────────────────────────────────────────

    @Test
    fun `User - property dapat diakses dengan benar`() {
        assertEquals(1, sampleUser.id)
        assertEquals("Budi Santoso", sampleUser.name)
        assertEquals("budi@email.com", sampleUser.email)
        assertEquals("user", sampleUser.role)
    }

    @Test
    fun `User - role admin tersimpan dengan benar`() {
        val adminUser = sampleUser.copy(role = "admin")
        assertEquals("admin", adminUser.role)
    }

    @Test
    fun `User - copy menghasilkan objek baru dengan property berbeda`() {
        val otherUser = sampleUser.copy(id = 99, name = "Siti")
        assertEquals(99, otherUser.id)
        assertEquals("Siti", otherUser.name)
        // Field lain tidak berubah
        assertEquals(sampleUser.email, otherUser.email)
    }

    @Test
    fun `User - dua objek identik dianggap equal`() {
        val user2 = sampleUser.copy()
        assertEquals(sampleUser, user2)
    }

    // ─── AuthData ────────────────────────────────────────────────────────────────

    @Test
    fun `AuthData - menyimpan token dan user dengan benar`() {
        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", sampleAuthData.token)
        assertEquals(sampleUser, sampleAuthData.user)
    }

    @Test
    fun `AuthData - token berbeda menghasilkan objek tidak equal`() {
        val other = sampleAuthData.copy(token = "other_token")
        assertFalse(sampleAuthData == other)
    }

    // ─── AuthResponse ────────────────────────────────────────────────────────────

    @Test
    fun `AuthResponse - sukses memiliki flag success true`() {
        assertTrue(sampleAuthResponse.success)
        assertEquals("Login berhasil", sampleAuthResponse.message)
    }

    @Test
    fun `AuthResponse - data berisi token dan user yang benar`() {
        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9", sampleAuthResponse.data.token)
        assertEquals("Budi Santoso", sampleAuthResponse.data.user.name)
    }

    @Test
    fun `AuthResponse - response gagal memiliki flag success false`() {
        val failResponse = sampleAuthResponse.copy(success = false, message = "Token tidak valid")
        assertFalse(failResponse.success)
        assertEquals("Token tidak valid", failResponse.message)
    }

    @Test
    fun `AuthResponse - akses nested data user berjenjang`() {
        // Akses: response -> data -> user -> role
        assertEquals("user", sampleAuthResponse.data.user.role)
        assertEquals(1, sampleAuthResponse.data.user.id)
    }
}
