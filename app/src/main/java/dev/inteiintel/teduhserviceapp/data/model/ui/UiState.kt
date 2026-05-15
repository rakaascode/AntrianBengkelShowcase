package dev.inteiintel.teduhserviceapp.data.model.ui

import dev.inteiintel.teduhserviceapp.data.model.User

sealed class UiState {
    object Loading: UiState()
    data class Success(val data: List<User>): UiState()
    data class Error(val message: String): UiState()
}


sealed class CreateUserState {
    object Idle : CreateUserState()
    object Loading : CreateUserState()
    data class Success(val user: User) : CreateUserState()
    data class Error(val message: String) : CreateUserState()
}