package dev.inteiintel.teduhserviceapp.repository

import dev.inteiintel.teduhserviceapp.data.model.DataWa
import dev.inteiintel.teduhserviceapp.data.model.ReminderModelsRequest
import dev.inteiintel.teduhserviceapp.data.model.ReminderModelsResponse
import dev.inteiintel.teduhserviceapp.data.remote.ApiServices
import dev.inteiintel.teduhserviceapp.data.repository.ReminderRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ReminderRepositoryTest {

    private lateinit var apiServices: ApiServices
    private lateinit var repository: ReminderRepository

    private val dummyNoWa = "6281234567890"

    private val dummySuccessResponse = ReminderModelsResponse(
        success = true,
        message = "Nomor WhatsApp berhasil disimpan",
        data = DataWa(user_id = 1, no_wa = dummyNoWa)
    )

    private val dummyFailResponse = ReminderModelsResponse(
        success = false,
        message = "Nomor WhatsApp tidak valid",
        data = DataWa(user_id = 0, no_wa = "")
    )

    @Before
    fun setUp() {
        apiServices = mockk()
        repository = ReminderRepository(apiServices)
    }

    // ─── sendWhatsappReminder ────────────────────────────────────────────────────

    @Test
    fun `sendWhatsappReminder - sukses mengembalikan ReminderModelsResponse`() = runTest {
        // Given
        coEvery { apiServices.updateNoWa(ReminderModelsRequest(no_wa = dummyNoWa)) } returns dummySuccessResponse

        // When
        val result = repository.sendWhatsappReminder(dummyNoWa)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(dummySuccessResponse, result.getOrNull())
    }

    @Test
    fun `sendWhatsappReminder - nomor WA tersimpan dalam response data`() = runTest {
        // Given
        coEvery { apiServices.updateNoWa(ReminderModelsRequest(no_wa = dummyNoWa)) } returns dummySuccessResponse

        // When
        val result = repository.sendWhatsappReminder(dummyNoWa)

        // Then
        assertEquals(dummyNoWa, result.getOrNull()?.data?.no_wa)
        assertEquals(1, result.getOrNull()?.data?.user_id)
    }

    @Test
    fun `sendWhatsappReminder - response success false mengembalikan Result failure`() = runTest {
        // Given
        coEvery { apiServices.updateNoWa(ReminderModelsRequest(no_wa = dummyNoWa)) } returns dummyFailResponse

        // When
        val result = repository.sendWhatsappReminder(dummyNoWa)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Nomor WhatsApp tidak valid", result.exceptionOrNull()?.message)
    }

    @Test
    fun `sendWhatsappReminder - exception jaringan mengembalikan Result failure`() = runTest {
        // Given
        coEvery { apiServices.updateNoWa(any()) } throws Exception("Socket timeout")

        // When
        val result = repository.sendWhatsappReminder(dummyNoWa)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Socket timeout", result.exceptionOrNull()?.message)
    }

    @Test
    fun `sendWhatsappReminder - request dikirim dengan nomor WA yang benar`() = runTest {
        // Given
        val expectedRequest = ReminderModelsRequest(no_wa = dummyNoWa)
        coEvery { apiServices.updateNoWa(expectedRequest) } returns dummySuccessResponse

        // When
        repository.sendWhatsappReminder(dummyNoWa)

        // Then
        coVerify(exactly = 1) { apiServices.updateNoWa(expectedRequest) }
    }

    @Test
    fun `sendWhatsappReminder - nomor WA format internasional diterima dengan benar`() = runTest {
        // Given
        val internationalNo = "6281999888777"
        val request = ReminderModelsRequest(no_wa = internationalNo)
        val response = ReminderModelsResponse(
            success = true,
            message = "Berhasil",
            data = DataWa(user_id = 2, no_wa = internationalNo)
        )
        coEvery { apiServices.updateNoWa(request) } returns response

        // When
        val result = repository.sendWhatsappReminder(internationalNo)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(internationalNo, result.getOrNull()?.data?.no_wa)
    }
}
