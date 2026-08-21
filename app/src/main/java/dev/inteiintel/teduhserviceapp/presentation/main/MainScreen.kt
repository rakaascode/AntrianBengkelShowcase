package dev.inteiintel.teduhserviceapp.presentation.main

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import dev.inteiintel.teduhserviceapp.ui.theme.MidnightBlue
import dev.inteiintel.teduhserviceapp.ui.theme.PrimBlue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 * Shell utama aplikasi setelah login berhasil.
 *
 * Menampilkan [BottomBar] dan mengatur navigasi internal antar tab:
 * - [HomeScreen] — beranda dengan ringkasan dan aksi utama.
 * - [QueuesScreen] — daftar antrian aktif pengguna.
 * - [NotificationsScreen] — broadcast notifikasi dari bengkel.
 * - [ProfileScreen] — profil dan pengaturan akun.
 *
 * Juga mengawasi refresh token secara reaktif: jika token dihapus (logout),
 * pengguna otomatis diarahkan kembali ke [Screen.Auth].
 *
 * @param viewModel ViewModel autentikasi untuk cek sesi aktif.
 * @param rootNavController Controller navigasi root (dari [AppNavGraph]) untuk navigasi lintas-alur.
 *
 * @see dev.inteiintel.teduhserviceapp.utils.navigation.AppNavGraph
 * @see AuthViewModel
 */
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



/**
 * Bottom navigation bar alternatif berbasis icon kustom dari drawable resource.
 *
 * Setiap item menggunakan ikon dari `res/drawable`. Tab aktif ditandai dengan
 * background [DarkOrange] dan ikon putih.
 *
 * @param navController Controller navigasi untuk bottom tab.
 *
 * @see BottomBar
 */
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


/**
 * Bottom navigation bar modern bergaya startup (Floating Pill Dock).
 *
 * Menggunakan floating container dengan sudut membulat, soft shadow,
 * dynamic animated active pill indicator, subtle haptic feel, dan label yang sleek.
 *
 * @param navController Controller navigasi internal bottom tab.
 */
@Composable
fun BottomBar(navController: NavHostController) {
    data class NavItem(
        val route: String,
        val label: String,
        val icon: androidx.compose.ui.graphics.vector.ImageVector
    )

    val items = listOf(
        NavItem(Screen.Home.route, "Beranda", Icons.Default.Home),
        NavItem(Screen.Queues.route, "Antrean", Icons.AutoMirrored.Filled.List),
        NavItem(Screen.Notifications.route, "Notifikasi", Icons.Default.Notifications),
        NavItem(Screen.Settings.route, "Profil", Icons.Default.Person)
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = Color.White,
        shadowElevation = 0.dp,
        tonalElevation = 0.dp
    ) {
        Column {
            // Divider tipis di atas bar
            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFFE8ECF4)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    val isSelected = currentRoute == item.route

                    val contentColor by androidx.compose.animation.animateColorAsState(
                        targetValue = if (isSelected) MidnightBlue else Color(0xFFADB5C7),
                        animationSpec = androidx.compose.animation.core.tween(durationMillis = 200),
                        label = "contentColor"
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                if (!isSelected) {
                                    navController.navigate(item.route) {
                                        popUpTo(Screen.Home.route) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = contentColor,
                                modifier = Modifier.size(22.dp)
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = item.label,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = contentColor,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}