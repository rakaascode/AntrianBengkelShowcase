package dev.inteiintel.teduhserviceapp.utils.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.inteiintel.teduhqueuesapp.ui.main.MainScreen
import dev.inteiintel.teduhserviceapp.data.local.TokenManager
import dev.inteiintel.teduhserviceapp.presentation.auth.AuthLoginScreen

@Composable
fun AppNavGraph(){

    var startDestination by remember { mutableStateOf<String?>(null) }
    val context= LocalContext.current


    LaunchedEffect(Unit) {
        val isLogin = TokenManager(context).isLoggedIn()
        startDestination = if (isLogin) {
            Screen.Main.route
        } else {
            Screen.Auth.route
        }
    }

    if (startDestination != null) {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = startDestination!!
        ) {
            composable(Screen.Auth.route) {
                AuthLoginScreen(navController = navController)
            }
            composable(Screen.Main.route) {
                MainScreen(navController = navController)
            }
        }
    }
}