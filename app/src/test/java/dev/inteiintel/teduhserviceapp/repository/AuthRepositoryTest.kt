package dev.inteiintel.teduhserviceapp.repository

import dev.inteiintel.teduhserviceapp.data.model.AuthData
import dev.inteiintel.teduhserviceapp.data.model.AuthResponse
import dev.inteiintel.teduhserviceapp.data.model.GoogleLoginRequest
import dev.inteiintel.teduhserviceapp.data.model.User
import dev.inteiintel.teduhserviceapp.data.remote.ApiServices
import dev.inteiintel.teduhserviceapp.data.repository.AuthRepository
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

class AuthRepositoryTest {

    private lateinit var apiServices: ApiServices
    private lateinit var authRepository: AuthRepository

    // Helper: dummy AuthResponse
    private val dummyUser = User(id = 1, name = "Budi", email = "budi@email.com", role = "user")
    private val dummyAuthData = AuthData(token = "token_abc123", user = dummyUser)
    private val dummyAuthResponse = AuthResponse(
        success = true,
        message = "Login berhasil",
        data = dummyAuthData
    )

    @Before
    fun setUp() {
        apiServices = mockk()
        authRepository = AuthRepository(apiServices)
    }

    // ─── loginGoogle ────────────────────────────────────────────────────────────

    @Test
    fun `loginGoogle - sukses mengembalikan Result success dengan AuthResponse`() = runTest {
        // Given
        val idToken = "valid_google_id_token"
        coEvery { apiServices.loginGoogle(GoogleLoginRequest(idToken)) } returns
                Response.success(dummyAuthResponse)

        // When
        val result = authRepository.loginGoogle(idToken)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(dummyAuthResponse, result.getOrNull())
        coVerify(exactly = 1) { apiServices.loginGoogle(GoogleLoginRequest(idToken)) }
    }

    @Test
    fun `loginGoogle - response body null mengembalikan Result failure`() = runTest {
        // Given
        val idToken = "valid_google_id_token"
        coEvery { apiServices.loginGoogle(GoogleLoginRequest(idToken)) } returns
                Response.success(null)

        // When
        val result = authRepository.loginGoogle(idToken)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Response kosong dari server", result.exceptionOrNull()?.message)
    }

    @Test
    fun `loginGoogle - response HTTP error mengembalikan Result failure dengan pesan error`() = runTest {
        // Given
        val idToken = "invalid_token"
        val errorResponse = Response.error<AuthResponse>(401, "Unauthorized".toResponseBody())
        coEvery { apiServices.loginGoogle(GoogleLoginRequest(idToken)) } returns errorResponse

        // When
        val result = authRepository.loginGoogle(idToken)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("401") == true)
    }

    @Test
    fun `loginGoogle - exception jaringan mengembalikan Result failure`() = runTest {
        // Given
        val idToken = "token"
        val exception = Exception("Koneksi terputus")
        coEvery { apiServices.loginGoogle(GoogleLoginRequest(idToken)) } throws exception

        // When
        val result = authRepository.loginGoogle(idToken)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Koneksi terputus", result.exceptionOrNull()?.message)
    }

    @Test
    fun `loginGoogle - token dalam AuthData dikembalikan dengan benar`() = runTest {
        // Given
        val idToken = "my_token"
        coEvery { apiServices.loginGoogle(GoogleLoginRequest(idToken)) } returns
                Response.success(dummyAuthResponse)

        // When
        val result = authRepository.loginGoogle(idToken)

        // Then
        assertEquals("token_abc123", result.getOrNull()?.data?.token)
        assertEquals("Budi", result.getOrNull()?.data?.user?.name)
    }
}
