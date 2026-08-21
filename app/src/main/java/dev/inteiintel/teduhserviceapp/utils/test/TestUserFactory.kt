//package dev.inteiintel.teduhserviceapp.utils.test
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import dev.inteiintel.teduhserviceapp.data.repository.UserRepository
//
//class TestViewModelApiFactory(
//    private val repo: UserRepository
//) : ViewModelProvider.Factory {
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(TestViewModelApi::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return TestViewModelApi(repo) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}