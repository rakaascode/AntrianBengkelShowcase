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
import retrofit2.http.PUT

/**
 * Interface Retrofit yang mendefinisikan seluruh endpoint REST API Teduh Service.
 *
 * Digunakan oleh semua repository di layer `data/repository`. Instance dibuat oleh
 * [ApiClient.create] dan disediakan sebagai singleton melalui [AppModule].
 *
 * Base URL: `https://rakaascode.site/api/`
 *
 * @see dev.inteiintel.teduhserviceapp.data.remote.ApiClient
 * @see dev.inteiintel.teduhserviceapp.di.AppModule
 */
interface ApiServices {

    /** Mengambil profil lengkap pengguna yang sedang login. */
    @GET("user/profile")
    suspend fun getUserProfile(): UserProfileResponse

    /**
     * Memperbarui informasi profil pengguna.
     *
     * @param request Data profil baru (nama, alamat, kota, provinsi, kode pos, promo_aktif, avatar_url).
     */
    @PUT("user/profile")
    suspend fun updateProfile(
        @Body request: dev.inteiintel.teduhserviceapp.data.model.UpdateProfileRequest
    ): UserProfileResponse

    /**
     * Mengunggah file foto profil baru pengguna.
     *
     * @param avatar MultipartBody.Part berisi file gambar.
     */
    @retrofit2.http.Multipart
    @POST("user/avatar")
    suspend fun uploadAvatar(
        @retrofit2.http.Part avatar: okhttp3.MultipartBody.Part
    ): UserProfileResponse

    /** Mengambil daftar semua cabang bengkel yang tersedia. */
    @GET("cabang")
    suspend fun getAllBranch(): BranchResponse

    /** Mengambil data antrian aktif milik pengguna saat ini. */
    @GET("antrian/me")
    suspend fun getAntreanActive(): AntreanActiveModelResponse

    /**
     * Mengambil nomor WhatsApp user yang sudah tersimpan.
     *
     * @return Response berisi nomor WA jika ada.
     */
    @GET("users/kontak")
    suspend fun getNoWa(): Response<ReminderModelsResponse>

    /**
     * Menyimpan nomor WhatsApp pengguna untuk pertama kali.
     *
     * @param request Body berisi nomor WA baru.
     */
    @POST("users/kontak")
    suspend fun simpanNoWa(
        @Body request: ReminderModelsRequest
    ): ReminderModelsResponse

    /**
     * Memperbarui nomor WhatsApp pengguna yang sudah ada.
     *
     * @param request Body berisi nomor WA yang diperbarui.
     */
    @PUT("users/kontak")
    suspend fun updateNoWa(
        @Body request: ReminderModelsRequest
    ): ReminderModelsResponse

    /** Mengambil semua notifikasi/broadcast dari bengkel. */
    @GET("broadcast")
    suspend fun getAllNotifications(): NotificationResponse

    /** Mengambil ringkasan antrian per cabang untuk ditampilkan di beranda. */
    @GET("cabang/antrian/ringkasan")
    suspend fun getAllRingkasan(): RingkasanHomeResponse

    /**
     * Membatalkan antrian yang sedang aktif berdasarkan ID.
     *
     * @param id ID antrian yang akan dibatalkan.
     * @return Response dengan pesan konfirmasi pembatalan.
     */
    @DELETE("antrian/{id}/batal")
    suspend fun batalAntrean(
        @Path("id") id: Int
    ): Response<MessageResponse>

    /**
     * Mengambil detail satu notifikasi berdasarkan ID.
     *
     * @param id ID notifikasi yang akan diambil.
     */
    @GET("broadcast/{id}")
    suspend fun getNotificationsById(
        @Path("id") id: Int
    ): DetailNotificationsResponse

    /**
     * Mengambil detail cabang bengkel berdasarkan ID, termasuk estimasi antrian.
     *
     * @param branchId ID cabang yang akan diambil.
     */
    @GET("cabang/{branchId}")
    suspend fun getById(
        @Path("branchId") branchId: Int
    ): DetailBranchResponse

    /**
     * Membuat antrian baru untuk pengguna.
     *
     * @param request Data lengkap antrian termasuk kendaraan, cabang, dan estimasi waktu.
     * @return Response dengan nomor antrian yang ditetapkan.
     */
    @POST("antrian")
    suspend fun ambilAntreanUsers(
        @Body request: CreateAntrianRequest
    ): Response<CreateAntrianResponse>

    /**
     * Autentikasi pengguna melalui Google ID token.
     *
     * @param request Body berisi ID token Google dari Credential Manager.
     * @return Response berisi JWT aplikasi jika berhasil.
     */
    @POST("auth/google")
    suspend fun loginGoogle(
        @Body request: GoogleLoginRequest
    ): Response<AuthResponse>
}