package dev.inteiintel.teduhserviceapp.data.model


data class ReminderModelsRequest(
    val no_wa: String
)

data class ReminderModelsResponse(
    val success: Boolean,
    val message: String,
    val data: DataWa
)



data class DataWa(
    val user_id: Int,
    val no_wa: String,
)