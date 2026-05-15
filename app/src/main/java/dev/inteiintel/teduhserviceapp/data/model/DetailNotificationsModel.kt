package dev.inteiintel.teduhserviceapp.data.model

data class DetailNotificationsResponse(
    val success: Boolean,
    val data: DetailNotificationData
)

data class DetailNotificationData(
    val id: Int,
    val judul: String,
    val deskripsi: String,
    val detail: String,
    val gambar_url: String,
    val tipe: String,
    val cabang_id: String?,
    val created_at: String,
)
