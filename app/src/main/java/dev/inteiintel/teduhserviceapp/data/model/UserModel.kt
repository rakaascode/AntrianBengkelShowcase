package dev.inteiintel.teduhserviceapp.data.model

data class UserProfileResponse(
    val success: Boolean,
    val data: UserData
)

data class UserData(
    val id: Int,
    val name: String,
    val email: String,
    val avatar_url: String,
    val role: String,
    val created_at: String,
    val antrian: List<Antrian>
)

data class Cabang(
    val id: Int,
    val nama: String,
    val alamat: String,
    val kota: String,
    val no_telp: String
)