package dev.inteiintel.teduhserviceapp.presentation.main.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.inteiintel.teduhqueuesapp.ui.main.LogoutButtonTest
import dev.inteiintel.teduhserviceapp.presentation.auth.AuthViewModel


@Composable
fun SettingsScreen(viewModel: AuthViewModel= viewModel(), navController: NavController){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Settings Screen")
        LogoutButtonTest(
            scope = scope,
            context=context,
            navController = navController,
            viewModel = viewModel

        )
    }
}