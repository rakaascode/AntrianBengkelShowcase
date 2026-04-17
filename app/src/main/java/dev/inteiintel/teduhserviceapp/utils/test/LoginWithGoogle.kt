package dev.inteiintel.teduhserviceapp.utils.test

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices.PIXEL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.inteiintel.teduhserviceapp.presentation.auth.AuthViewModel

@Composable
fun LoginWithGoogle(viewModel: AuthViewModel = viewModel()){
    val context = LocalContext.current
    val loading by viewModel.loading.collectAsState()

    Column (modifier = Modifier.fillMaxSize().fillMaxHeight().padding(horizontal = 20.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

        if (loading){
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(20.dp))
            Text("Loading...", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(10.dp))
//
//        if (!token.isNullOrEmpty()) {
//            Text(text = token.toString().take(20))
//        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            viewModel.onLoginClick (context){ message ->
                Toast.makeText(context,message, Toast.LENGTH_LONG).show()
            }
        }) {
            Text("Login With Google")
        }

    }

}

@Preview(showBackground = true, showSystemUi = true, device = PIXEL )
@Composable
fun PreviewLoginWithGoogle(){
    LoginWithGoogle()
}