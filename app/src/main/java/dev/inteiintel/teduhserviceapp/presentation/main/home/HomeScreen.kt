package dev.inteiintel.teduhserviceapp.presentation.main.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import dev.inteiintel.teduhserviceapp.R
import dev.inteiintel.teduhserviceapp.data.model.UserData
import dev.inteiintel.teduhserviceapp.presentation.main.home.detail_cabang.daftar_cabang.DaftarCabangViewModel
import dev.inteiintel.teduhserviceapp.presentation.main.profile.ProfileViewModel
import dev.inteiintel.teduhserviceapp.ui.theme.PrimBlue
import dev.inteiintel.teduhserviceapp.utils.navigation.Screen
import java.util.Calendar


@Composable
fun HomeScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    daftarCabangViewModel: DaftarCabangViewModel = hiltViewModel(),
    navHostController: NavHostController
) {

    val profile = profileViewModel.getProfile.collectAsState().value

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
        daftarCabangViewModel.loadDataCabang()
    }



    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        item {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                HeaderSection(profile)

                Spacer(modifier = Modifier.height(16.dp))

                PromoBanner(navHostController)

                Spacer(modifier = Modifier.height(16.dp))

                MenuGrid(navHostController)
            }
        }
    }
}

@Composable
fun HeaderSection(user: UserData?) {

    val greeting = greetingState().value

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {

        Column {

            Text(
                text = "Halo, ${user?.name ?: ""}",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Text(
                text = greeting,
                color = Color.Gray
            )
        }

        AsyncImage(
            model = user?.avatar_url,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )
    }
}


@Composable
fun PromoBanner(navController: NavController) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        Color(0xFF1C1F4A),
                        Color(0xFF2E3A87)
                    )
                )
            )
            .clickable {
                navController.navigate(Screen.DetailPromo.route)
            }
    ) {

        Image(
            painter = painterResource(id = R.drawable.img_promo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(alpha = 0.15f))
                    .clickable {
                        navController.navigate(Screen.DetailPromo.route)
                    }
                    .padding(
                        horizontal = 12.dp,
                        vertical = 6.dp
                    )
            ) {

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "Lihat Detail",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}

sealed class HomeMenuIcon {
    data class Vector(val imageVector: androidx.compose.ui.graphics.vector.ImageVector) : HomeMenuIcon()
    data class Drawable(val resId: Int) : HomeMenuIcon()
}

data class HomeMenuItem(
    val title: String,
    val route: String,
    val icon: HomeMenuIcon
)

@Composable
fun MenuGrid(navController: NavController) {

    val menuItems = listOf(
        HomeMenuItem("Cabang Terdekat", Screen.DaftarCabang.route, HomeMenuIcon.Drawable(R.drawable.ic_maps)),
        HomeMenuItem("Ambil Antrian", Screen.AmbilAntreanFromBeranda.route, HomeMenuIcon.Drawable(R.drawable.ic_que)),
        HomeMenuItem("Riwayat", Screen.Riyawat.route, HomeMenuIcon.Drawable(R.drawable.ic_history)),
        HomeMenuItem("Pengingat", Screen.Pengingat.route, HomeMenuIcon.Drawable(R.drawable.ic_clock)),
        HomeMenuItem("Pantau Antrian", Screen.PantauAntrian.route, HomeMenuIcon.Vector(Icons.Default.ConfirmationNumber)),
        HomeMenuItem("Panduan", Screen.Panduan.route, HomeMenuIcon.Drawable(R.drawable.ic_ob))
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        for (row in 0 until 3) {

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                for (col in 0 until 2) {

                    val index = row * 2 + col
                    val item = menuItems[index]

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(120.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    navController.navigate(item.route)
                                },
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(PrimBlue)
                            ) {

                                when (val icon = item.icon) {
                                    is HomeMenuIcon.Vector -> {
                                        Icon(
                                            imageVector = icon.imageVector,
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp),
                                            tint = Color.White
                                        )
                                    }
                                    is HomeMenuIcon.Drawable -> {
                                        Icon(
                                            painter = painterResource(id = icon.resId),
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp),
                                            tint = Color.White
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = item.title,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getGreeting(): String {

    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

    return when (hour) {
        in 5..10 -> "Selamat Pagi ☀️"
        in 11..14 -> "Selamat Siang 🌤️"
        in 15..17 -> "Selamat Sore 🌇"
        else -> "Selamat Malam 🌙"
    }
}

@Composable
fun greetingState(): State<String> {

    val greeting = remember {
        mutableStateOf(getGreeting())
    }

    LaunchedEffect(Unit) {

        while (true) {

            greeting.value = getGreeting()

            kotlinx.coroutines.delay(60_000)
        }
    }

    return greeting
}


