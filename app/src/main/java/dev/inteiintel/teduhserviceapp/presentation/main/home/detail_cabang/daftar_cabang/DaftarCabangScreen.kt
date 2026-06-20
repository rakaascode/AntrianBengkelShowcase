package dev.inteiintel.teduhserviceapp.presentation.main.home.detail_cabang.daftar_cabang

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dev.inteiintel.teduhserviceapp.R
import dev.inteiintel.teduhserviceapp.data.model.BranchWithDistance
import dev.inteiintel.teduhserviceapp.ui.theme.DarkOrange
import dev.inteiintel.teduhserviceapp.ui.theme.DarkSlate
import dev.inteiintel.teduhserviceapp.ui.theme.DimGray
import dev.inteiintel.teduhserviceapp.ui.theme.MidnightBlue
import dev.inteiintel.teduhserviceapp.ui.theme.PrimBlue
import dev.inteiintel.teduhserviceapp.ui.theme.SnowWhite
import dev.inteiintel.teduhserviceapp.utils.LocationUtils
import dev.inteiintel.teduhserviceapp.utils.navigation.Screen
import kotlinx.coroutines.launch


@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DaftarCabangScreen(
    navController: NavController,
    daftarCabangViewModel: DaftarCabangViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val cabangSorted = daftarCabangViewModel.cabangSorted.collectAsState().value
    val cabangRaw    = daftarCabangViewModel.cabang.collectAsState().value
    val hasLocation  = daftarCabangViewModel.hasLocation.collectAsState().value

    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    // Minta izin dan ambil lokasi untuk sort
    LaunchedEffect(Unit) {
        locationPermissionState.launchPermissionRequest()
    }

    LaunchedEffect(locationPermissionState.status, cabangRaw) {
        if (locationPermissionState.status.isGranted && cabangRaw.isNotEmpty()) {
            LocationUtils.getUserLocation(
                context = context,
                onResult = { lat, lng ->
                    daftarCabangViewModel.sortByDistance(lat, lng)
                },
                onError = {
                    // Jika lokasi gagal, tetap tampilkan cabang tanpa sort
                }
            )
        }
    }

    Column {
        // ─── Top Bar ──────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .background(SnowWhite)
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Daftar Cabang",
                    fontWeight = FontWeight.Bold,
                    color = DarkSlate
                )
            }

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = DarkSlate
                )
            }
        }

        HorizontalDivider(thickness = 1.dp, color = DimGray)

        // ─── List Cabang ──────────────────────────────────────────────────────
        if (hasLocation && cabangSorted.isNotEmpty()) {
            // Tampilkan hasil sort dari terdekat
            CardBranchItemsSorted(
                branchList = cabangSorted,
                navController = navController
            )
        } else {
            // Fallback: tampilkan tanpa sort jika lokasi belum ada
            CardBranchItemsSorted(
                branchList = cabangRaw.filterNotNull().map {
                    BranchWithDistance(branch = it, distanceKm = null)
                },
                navController = navController
            )
        }
    }
}

@Composable
fun CardBranchItemsSorted(
    branchList: List<BranchWithDistance>,
    navController: NavController
) {
    LazyColumn {
        items(branchList) { item ->
            val branch = item.branch

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = SnowWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {

                    Spacer(Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = branch.nama,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        // Tampilkan jarak nyata jika tersedia
                        val jarakText = item.distanceKm?.let {
                            if (it == Double.MAX_VALUE) "-"
                            else "${"%.1f".format(it)} KM"
                        } ?: "-"

                        Text(
                            text = jarakText,
                            fontSize = 13.sp,
                            color = PrimBlue,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Row {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = DarkOrange,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = branch.alamat,
                            fontSize = 13.sp,
                            color = Color(0xFF6B7280)
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Box(modifier = Modifier.fillMaxWidth()) {
                        Image(
                            painterResource(R.drawable.img_kantor),
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier.aspectRatio(2f)
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Row {
                        OutlinedButton(
                            onClick = {
                                navController.navigate(Screen.DetailCabang.createRoute(branch.id))
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Lihat Detail")
                        }

                        Spacer(Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(MidnightBlue)
                        ) {
                            IconButton(onClick = {}) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = SnowWhite
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}