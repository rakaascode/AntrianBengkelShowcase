package dev.inteiintel.teduhserviceapp.presentation.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.inteiintel.teduhserviceapp.data.model.UpdateProfileRequest
import dev.inteiintel.teduhserviceapp.data.model.UserData
import dev.inteiintel.teduhserviceapp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject

sealed class ProfileUiState {
    object Idle : ProfileUiState()
    object Loading : ProfileUiState()
    data class Success(val data: UserData) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    private val _profile = MutableStateFlow<UserData?>(null)
    val getProfile = _profile.asStateFlow()

    private val _updateState = MutableStateFlow<ProfileUiState>(ProfileUiState.Idle)
    val updateState = _updateState.asStateFlow()

    fun loadProfile() {
        viewModelScope.launch {
            val result = userRepository.userProfile()

            result.onSuccess {
                _profile.value = it
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun updateProfile(
        name: String,
        alamat: String?,
        kota: String?,
        provinsi: String?,
        kodePos: String?,
        promoAktif: Boolean? = null,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _updateState.value = ProfileUiState.Loading

            val request = UpdateProfileRequest(
                name = name.ifBlank { null },
                alamat = alamat?.ifBlank { null },
                kota = kota?.ifBlank { null },
                provinsi = provinsi?.ifBlank { null },
                kode_pos = kodePos?.ifBlank { null },
                promo_aktif = promoAktif
            )

            val result = userRepository.updateProfile(request)
            result.onSuccess { updatedUser ->
                _profile.value = updatedUser
                _updateState.value = ProfileUiState.Success(updatedUser)
                onSuccess()
            }.onFailure { error ->
                _updateState.value = ProfileUiState.Error(error.message ?: "Gagal memperbarui profil")
            }
        }
    }

    fun uploadAvatar(
        context: android.content.Context,
        imageUri: android.net.Uri,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _updateState.value = ProfileUiState.Loading
            try {
                val contentResolver = context.contentResolver
                val inputStream = contentResolver.openInputStream(imageUri)
                val tempFile = java.io.File.createTempFile("avatar_upload", ".jpg", context.cacheDir)
                val outputStream = java.io.FileOutputStream(tempFile)
                inputStream?.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }

                val mimeType = contentResolver.getType(imageUri) ?: "image/jpeg"
                val mediaType = mimeType.toMediaTypeOrNull()
                val requestFile = tempFile.asRequestBody(mediaType)
                val body = okhttp3.MultipartBody.Part.createFormData("avatar", tempFile.name, requestFile)

                val result = userRepository.uploadAvatar(body)
                result.onSuccess { updatedUser ->
                    _profile.value = updatedUser
                    _updateState.value = ProfileUiState.Success(updatedUser)
                    onSuccess()
                }.onFailure { error ->
                    _updateState.value = ProfileUiState.Error(error.message ?: "Gagal mengunggah foto profil")
                }
            } catch (e: Exception) {
                _updateState.value = ProfileUiState.Error(e.message ?: "Gagal memproses gambar")
            }
        }
    }

    fun resetUpdateState() {
        _updateState.value = ProfileUiState.Idle
    }
}