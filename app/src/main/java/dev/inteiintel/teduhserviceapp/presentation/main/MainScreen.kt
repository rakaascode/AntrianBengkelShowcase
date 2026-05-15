package dev.inteiintel.teduhqueuesapp.ui.main

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.inteiintel.teduhserviceapp.R
import dev.inteiintel.teduhserviceapp.utils.navigation.Screen
import dev.inteiintel.teduhserviceapp.presentation.auth.AuthViewModel
import dev.inteiintel.teduhserviceapp.presentation.main.home.HomeScreen
import dev.inteiintel.teduhserviceapp.presentation.main.notifications.NotificationsScreen
import dev.inteiintel.teduhserviceapp.presentation.main.queues.QueuesScreen
import dev.inteiintel.teduhserviceapp.presentation.main.profile.ProfileScreen
import dev.inteiintel.teduhserviceapp.ui.theme.DarkOrange
import dev.inteiintel.teduhserviceapp.ui.theme.DimGray
import dev.inteiintel.teduhserviceapp.ui.theme.GhostWhite
import dev.inteiintel.teduhserviceapp.ui.theme.PrimBlue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    viewModel: AuthViewModel = viewModel(),
    rootNavController: NavController
) {
    val context = LocalContext.current

    val bottomNavController = rememberNavController()

    LaunchedEffect(Unit) {
        viewModel.getCurrentRefreshToken(context).collect { token ->
            if (token.isEmpty()) {
                rootNavController.navigate(Screen.Auth.route) {
                    popUpTo(Screen.Main.route) {
                        inclusive = true
                    }
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomBar(bottomNavController)
        }
    ) { padding ->

        NavHost(
            navController = bottomNavController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Home.route) { HomeScreen(navHostController = rootNavController as NavHostController) }
            composable(Screen.Queues.route) { QueuesScreen(navController = rootNavController) }
            composable(Screen.Notifications.route) { NotificationsScreen(navController = rootNavController) }
            composable(Screen.Settings.route) { ProfileScreen(navController = bottomNavController, toDetail = rootNavController) }
        }
    }
}

@Composable
fun LogoutButtonTest(scope: CoroutineScope, viewModel: AuthViewModel, context: Context, navController: NavController ) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
        Screen.Home.route to R.drawable.ic_home,
        Screen.Queues.route to R.drawable.ic_queue,
        Screen.Notifications.route to R.drawable.ic_bell,
        Screen.Settings.route to R.drawable.ic_settings
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


@Composable
fun BottomBar(navController: NavHostController) {

    val items = listOf(
        Screen.Home.route to Icons.Default.Home,
        Screen.Queues.route to Icons.AutoMirrored.Filled.List,
        Screen.Notifications.route to Icons.Default.Notifications,
        Screen.Settings.route to Icons.Default.Person
    )
    Column {
        HorizontalDivider(
            thickness = 1.dp,
            color = DimGray
        )
        NavigationBar(containerColor = Color(0xFFFFFFFF)) {
            val currentRoute =
                navController.currentBackStackEntryAsState().value?.destination?.route

            items.forEach { (route, icon) ->

                NavigationBarItem(
                    selected = currentRoute == route,
                    onClick = {
                        navController.navigate(route) {

                            popUpTo(Screen.Home.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },

                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent,
                        selectedIconColor = PrimBlue,
                        selectedTextColor = PrimBlue,
                        unselectedIconColor = DimGray.copy(alpha = 0.6f),
                        unselectedTextColor = DimGray.copy(alpha = 0.6f)
                    ),
                    icon = {
                        Icon(icon, contentDescription = route)
                    },
                    label = {
                        Text(route.replaceFirstChar { it.uppercase() })
                    }
                )
            }
        }
    }


}