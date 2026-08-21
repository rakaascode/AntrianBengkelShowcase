package dev.inteiintel.teduhserviceapp.data.model

data class UserProfileResponse(
    val success: Boolean,
    val data: UserData
)

data class UserData(
    val id: Int,
    val name: String,
    val email: String?,
    val avatar_url: String?,
    val role: String,
    val alamat: String? = null,
    val kota: String? = null,
    val provinsi: String? = null,
    val kode_pos: String? = null,
    val promo_aktif: Boolean? = false,
    val no_wa: String? = null,
    val created_at: String? = null,
    val antrian: List<Antrian> = emptyList()
)

data class UpdateProfileRequest(
    val name: String? = null,
    val avatar_url: String? = null,
    val alamat: String? = null,
    val kota: String? = null,
    val provinsi: String? = null,
    val kode_pos: String? = null,
    val promo_aktif: Boolean? = null
)

data class Cabang(
    val id: Int,
    val nama: String,
    val alamat: String,
    val kota: String,
    val no_telp: String
)