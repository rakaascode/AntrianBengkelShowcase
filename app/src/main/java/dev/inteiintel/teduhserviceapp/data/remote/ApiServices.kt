package dev.inteiintel.teduhserviceapp.data.remote

import dev.inteiintel.teduhserviceapp.data.model.AntreanActiveModelResponse
import dev.inteiintel.teduhserviceapp.data.model.AuthResponse
import dev.inteiintel.teduhserviceapp.data.model.BranchResponse
import dev.inteiintel.teduhserviceapp.data.model.CreateAntrianRequest
import dev.inteiintel.teduhserviceapp.data.model.CreateAntrianResponse
import dev.inteiintel.teduhserviceapp.data.model.DetailBranchResponse
import dev.inteiintel.teduhserviceapp.data.model.DetailNotificationsResponse
import dev.inteiintel.teduhserviceapp.data.model.GoogleLoginRequest
import dev.inteiintel.teduhserviceapp.data.model.MessageResponse
import dev.inteiintel.teduhserviceapp.data.model.NotificationResponse
import dev.inteiintel.teduhserviceapp.data.model.ReminderModelsRequest
import dev.inteiintel.teduhserviceapp.data.model.ReminderModelsResponse
import dev.inteiintel.teduhserviceapp.data.model.RingkasanHomeResponse
import dev.inteiintel.teduhserviceapp.data.model.UserProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.DELETE

interface ApiServices {

    @GET("users/profile")
    suspend fun getUserProfile(): UserProfileResponse


    @GET("cabang")
    suspend fun getAllBranch(): BranchResponse

    @GET("antrian/me")
    suspend fun getAntreanActive(): AntreanActiveModelResponse

    @POST("users/kontak")
    suspend fun updateNoWa(
        @Body request: ReminderModelsRequest
    ): ReminderModelsResponse

    @GET("broadcast")
    suspend fun getAllNotifications(): NotificationResponse

    @GET("cabang/antrian/ringkasan")
    suspend fun getAllRingkasan(): RingkasanHomeResponse

    @DELETE("antrian/{id}/batal")
    suspend fun batalAntrean(
        @Path("id") id: Int
    ): Response<MessageResponse>

    @GET("broadcast/{id}")
    suspend fun getNotificationsById(
        @Path("id") id: Int
    ): DetailNotificationsResponse

    @GET("cabang/{branchId}")
    suspend fun getById(
        @Path("branchId") branchId: Int
    ): DetailBranchResponse


    @POST("antrian")
    suspend fun ambilAntreanUsers(
        @Body request: CreateAntrianRequest
    ): Response<CreateAntrianResponse>


    @POST("auth/google")
    suspend fun loginGoogle(
        @Body request: GoogleLoginRequest
    ): Response<AuthResponse>
}