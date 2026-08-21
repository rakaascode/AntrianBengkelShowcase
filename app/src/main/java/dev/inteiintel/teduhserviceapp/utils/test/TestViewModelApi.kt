//package dev.inteiintel.teduhserviceapp.utils.test
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import dev.inteiintel.teduhserviceapp.data.model.CreateUserRequest
//import dev.inteiintel.teduhserviceapp.data.model.ui.CreateUserState
//import dev.inteiintel.teduhserviceapp.data.model.ui.UiState
//import dev.inteiintel.teduhserviceapp.data.repository.UserRepository
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//
//class TestViewModelApi(private val repo: UserRepository) : ViewModel() {
//
//    private val _state = MutableStateFlow<UiState>(UiState.Loading)
//    val state: StateFlow<UiState> = _state
//
//    private val _createState = MutableStateFlow<CreateUserState>(CreateUserState.Idle)
//    val createState: StateFlow<CreateUserState> = _createState
//
//    init {
//        fetchUser()
//    }
//
//    fun createUser(name: String, age: Int){
//        viewModelScope.launch {
//            _createState.value = CreateUserState.Loading
//            val result = repo.createUser(CreateUserRequest(name, age))
//
//            result.fold(
//                onSuccess = {
//                    _createState.value = CreateUserState.Success(it)
//
//                    fetchUser()
//                },
//                onFailure = {
//                    _createState.value = CreateUserState.Error("Error: ${it.message}") // ✅ WAJIB
//                }
//            )
//
//        }
//    }
//
//
//    private fun fetchUser(){
//        viewModelScope.launch {
//            try {
//                _state.value = UiState.Loading
//                val users = repo.getUser()
//                _state.value = UiState.Success(users)
//            } catch (e: Exception){
//                _state.value = UiState.Error(e.message ?: "Unknown Error")
//            }
//        }
//    }
//}