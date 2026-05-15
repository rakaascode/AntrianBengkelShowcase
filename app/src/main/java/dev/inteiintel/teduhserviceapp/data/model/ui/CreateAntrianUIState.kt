package dev.inteiintel.teduhserviceapp.data.model.ui

data class CreateAntrianUiState(

    val isLoading: Boolean = false,

    val isSuccess: Boolean = false,

    val errorMessage: String? = null,

    val nomorAntrean: String = ""
)
