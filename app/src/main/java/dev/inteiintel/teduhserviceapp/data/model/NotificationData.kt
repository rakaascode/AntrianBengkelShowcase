package dev.inteiintel.teduhserviceapp.data.model

data class NotificationResponse(
    val success: Boolean,
    val data: List<NotificationData>
)


data class NotificationData(
    val id: Int,
    val judul: String?,
    val deskripsi: String?,
    val tipe: String?,
    val cabang_id: String?,
    val created_at: String?,
    val isRead: Boolean = false
)