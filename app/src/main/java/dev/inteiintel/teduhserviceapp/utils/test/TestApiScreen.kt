//package dev.inteiintel.teduhserviceapp.utils.test
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.Button
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import dev.inteiintel.teduhserviceapp.data.model.ui.CreateUserState
//import dev.inteiintel.teduhserviceapp.data.model.ui.UiState
//import dev.inteiintel.teduhserviceapp.data.remote.ApiClient
//import dev.inteiintel.teduhserviceapp.data.repository.UserRepository
//
//@Composable
//fun TestApiScreen() {
//
//    val api = ApiClient.api
//    val repo = UserRepository(api)
//
//    val viewModelApi: TestViewModelApi = viewModel(
//        factory = TestViewModelApiFactory(repo)
//    )
//
//    val state by viewModelApi.state.collectAsState()
//    val createState by viewModelApi.createState.collectAsState()
//
//    var name by remember { mutableStateOf("") }
//    var age by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//
//        when (state) {
//            is UiState.Loading -> CircularProgressIndicator()
//
//            is UiState.Success -> {
//                val users = (state as UiState.Success).data
//                LazyColumn {
//                    items(users) { user ->
//                        Text(text = user.name)
//                    }
//                }
//            }
//
//            is UiState.Error -> {
//                Text("Error: ${(state as UiState.Error).message}")
//            }
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        when (createState) {
//            is CreateUserState.Loading -> CircularProgressIndicator()
//            is CreateUserState.Success -> Text("Success")
//            is CreateUserState.Error -> Text("Error")
//            else -> {}
//        }
//
//        TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
//
//        TextField(value = age, onValueChange = { age = it }, label = { Text("Age") })
//
//        Button(onClick = {
//            val ageInt = age.toIntOrNull()
//            if (ageInt != null) {
//                viewModelApi.createUser(name, ageInt)
//            }
//        }) {
//            Text("Kirim")
//        }
//    }
//}