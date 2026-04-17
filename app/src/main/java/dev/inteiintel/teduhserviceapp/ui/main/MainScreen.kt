package dev.inteiintel.teduhqueuesapp.ui.main

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.inteiintel.teduhserviceapp.R
import dev.inteiintel.teduhserviceapp.navigation.Screen
import dev.inteiintel.teduhserviceapp.ui.auth.AuthViewModel
import dev.inteiintel.teduhserviceapp.ui.main.home.HomeScreen
import dev.inteiintel.teduhserviceapp.ui.main.notifications.NotificationsScreen
import dev.inteiintel.teduhserviceapp.ui.main.queues.QueuesScreen
import dev.inteiintel.teduhserviceapp.ui.main.settings.SettingsScreen
import dev.inteiintel.teduhserviceapp.ui.theme.DarkOrange
import dev.inteiintel.teduhserviceapp.ui.theme.GhostWhite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun MainScreen(
    viewModel: AuthViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current

    val bottomNavController = rememberNavController()

    LaunchedEffect(Unit) {
        viewModel.getCurrentRefreshToken(context).collect { token ->
            if (token.isEmpty()) {
                navController.navigate(Screen.Auth.route) {
                    popUpTo(Screen.Main.route) {
                        inclusive = true
                    }
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            CustomBottomBar(bottomNavController)
        }
    ) { padding ->

        NavHost(
            navController = bottomNavController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {
            composable("home") { HomeScreen() }
            composable("queues") { QueuesScreen() }
            composable("notif") { NotificationsScreen() }
            composable("settings") { SettingsScreen(navController = navController) }
        }
    }
}

@Composable
fun LogoutButtonTest(scope: CoroutineScope, viewModel: AuthViewModel, context: Context, navController: NavController ) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("HALAMANA UTAMA")

        Button(onClick = {
            scope.launch {
                viewModel.deleteToken(context)
                viewModel.getCurrentRefreshToken(context).collect { token ->
                    if (token.isEmpty()) {
                        navController.navigate(Screen.Auth.route) {
                            popUpTo(Screen.Main.route) {
                                inclusive = true
                            }
                        }
                    }
                }
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Logout")
        }
    }
}



@Composable
fun CustomBottomBar(navController: NavController) {

    val items = listOf(
        "home" to R.drawable.ic_home,
        "queues" to R.drawable.ic_queue,
        "notif" to R.drawable.ic_bell,
        "settings" to R.drawable.ic_settings
    )

    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(GhostWhite).shadow(elevation = 1.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            items.forEach { (route, icon) ->

                val isSelected = currentRoute == route

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (isSelected) DarkOrange else Color.Transparent
                        )
                        .clickable {
                            navController.navigate(route) {
                                popUpTo("home") {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = null,
                        tint = if (isSelected) Color.White else Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}