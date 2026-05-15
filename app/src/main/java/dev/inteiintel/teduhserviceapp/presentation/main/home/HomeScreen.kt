package dev.inteiintel.teduhserviceapp.presentation.main.home

import android.Manifest
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dev.inteiintel.teduhserviceapp.R
import dev.inteiintel.teduhserviceapp.data.model.RingkasanCabangItem
import dev.inteiintel.teduhserviceapp.data.model.UserData
import dev.inteiintel.teduhserviceapp.presentation.main.home.detail_cabang.daftar_cabang.DaftarCabangViewModel
import dev.inteiintel.teduhserviceapp.presentation.main.profile.ProfileViewModel
import dev.inteiintel.teduhserviceapp.ui.theme.PrimBlue
import dev.inteiintel.teduhserviceapp.utils.LocationUtils
import dev.inteiintel.teduhserviceapp.utils.navigation.Screen
import java.util.Calendar

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    daftarCabangViewModel: DaftarCabangViewModel = hiltViewModel(),
    ringkasanViewModel: RingkasanHomeViewModel = hiltViewModel(),
    navHostController: NavHostController
) {

    val context = LocalContext.current

    val profile = profileViewModel.getProfile.collectAsState().value
    val nearestCabang = ringkasanViewModel.nearestCabang.collectAsState().value
    val loading = ringkasanViewModel.loading.collectAsState().value

    val showQueueCard = nearestCabang != null &&
            nearestCabang.nomorDipanggil != null &&
            nearestCabang.nomorDipanggil != 0

    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )

    LaunchedEffect(Unit) {
        locationPermissionState.launchPermissionRequest()
    }

    LaunchedEffect(Unit) {

        Log.d("HOME_DEBUG", "🚀 HomeScreen launched")

        profileViewModel.loadProfile()
        daftarCabangViewModel.loadDataCabang()

        if (locationPermissionState.status.isGranted) {

            LocationUtils.getUserLocation(
                context = context,
                onResult = { lat, lng ->

                    Log.d("HOME_DEBUG", "📍 LOCATION OK: $lat , $lng")

                    ringkasanViewModel.getRingkasanHome(
                        userLat = lat,
                        userLng = lng
                    )
                },
                onError = {
                    Log.e("HOME_DEBUG", "❌ LOCATION FAILED")
                }
            )

        } else {

            Log.e("HOME_DEBUG", "❌ Permission not granted")
        }
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

                if (loading) {


                } else {

                    AnimatedVisibility(
                        visible = showQueueCard,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {

                        nearestCabang?.let {
                            CurrentQueueCard(it)
                        }
                    }
                }

                AnimatedVisibility(
                    visible = showQueueCard,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                PromoBanner()

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
fun CurrentQueueCard(
    cabang: RingkasanCabangItem
) {

    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "ANTRIAN HARI INI",
                    color = Color.Black,
                    fontSize = 14.sp
                )

                Text(
                    text = cabang.namaCabang ?: "-",
                    fontSize = 14.sp,
                    color = Color.White,
                    modifier = Modifier
                        .background(
                            Color(0xFFFF7A00),
                            RoundedCornerShape(20)
                        )
                        .padding(
                            horizontal = 12.dp,
                            vertical = 4.dp
                        )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "A${cabang.nomorDipanggil}",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { 0.7f },
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF1C1F4A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = "Estimasi: ${cabang.estimasiJam ?: "-"}",
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Text(
                    text = "${cabang.sisaAntrian ?: 0} Antrian lagi",
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun PromoBanner() {

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
                    .clickable { }
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

@Composable
fun MenuGrid(navController: NavController) {

    val menus = listOf(
        "Cabang Terdekat",
        "Ambil Antrian",
        "Riwayat",
        "Pengingat"
    )

    val route = listOf(
        Screen.DaftarCabang.route,
        Screen.AmbilAntreanFromBeranda.route,
        Screen.Riyawat.route,
        Screen.Pengingat.route
    )

    val icons = listOf(
        R.drawable.ic_maps,
        R.drawable.ic_que,
        R.drawable.ic_history,
        R.drawable.ic_clock
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        for (row in 0 until 2) {

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                for (col in 0 until 2) {

                    val index = row * 2 + col

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(120.dp)
                            ,
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {

                        Column(
                            modifier = Modifier.fillMaxSize().clickable {
                                navController.navigate(route[index])
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

                                Icon(
                                    painter = painterResource(id = icons[index]),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = menus[index],
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


