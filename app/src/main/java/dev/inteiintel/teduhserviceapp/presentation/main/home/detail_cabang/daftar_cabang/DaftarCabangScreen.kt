package dev.inteiintel.teduhserviceapp.presentation.main.home.detail_cabang.daftar_cabang

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dev.inteiintel.teduhserviceapp.R
import dev.inteiintel.teduhserviceapp.data.model.BranchWithDistance
import dev.inteiintel.teduhserviceapp.ui.theme.DarkSlate
import dev.inteiintel.teduhserviceapp.ui.theme.MidnightBlue
import dev.inteiintel.teduhserviceapp.ui.theme.SnowWhite
import dev.inteiintel.teduhserviceapp.utils.LocationUtils
import dev.inteiintel.teduhserviceapp.utils.navigation.Screen

/**
 * Layar Daftar Cabang yang telah didesain ulang dengan gaya modern.
 *
 * Fitur:
 * - Search bar pencarian nama / alamat cabang.
 * - Deteksi lokasi user untuk menampilkan jarak real time & badge terdekat.
 * - Card cabang modern dengan foto, nama, alamat, chip jarak, dan quick buttons (Lihat Detail & Arah).
 */
@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DaftarCabangScreen(
    navController: NavController,
    daftarCabangViewModel: DaftarCabangViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val cabangSorted = daftarCabangViewModel.cabangSorted.collectAsState().value
    val cabangRaw = daftarCabangViewModel.cabang.collectAsState().value
    val hasLocation = daftarCabangViewModel.hasLocation.collectAsState().value

    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

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
                    // Fallback handled in UI
                }
            )
        }
    }

    val displayList = remember(cabangSorted, cabangRaw, hasLocation) {
        if (hasLocation && cabangSorted.isNotEmpty()) {
            cabangSorted
        } else {
            cabangRaw.filterNotNull().map {
                BranchWithDistance(branch = it, distanceKm = null)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SnowWhite)
    ) {
        // ─── Top Bar ──────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .background(SnowWhite)
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Daftar Cabang",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DarkSlate
                )
            }

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    modifier = Modifier.size(24.dp),
                    tint = DarkSlate
                )
            }
        }

        HorizontalDivider(thickness = 1.dp, color = Color(0xFFE8ECF4))

        // ─── Content / List Cabang ────────────────────────────────────────────
        when {
            cabangRaw.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = MidnightBlue)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Memuat daftar cabang...",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            displayList.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Cabang tidak ditemukan",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Semua Lokasi (${displayList.size})",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkSlate
                            )

                            if (hasLocation) {
                                Text(
                                    text = "Diurutkan dari terdekat",
                                    fontSize = 11.sp,
                                    color = Color(0xFF2563EB),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    items(displayList) { item ->
                        ModernBranchCard(
                            item = item,
                            onCardClick = {
                                navController.navigate(Screen.DetailCabang.createRoute(item.branch.id))
                            },
                            onDirectionsClick = {
                                val lat = item.branch.latitude
                                val lng = item.branch.longitude
                                val gmmIntentUri = Uri.parse("google.navigation:q=$lat,$lng")
                                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                                    setPackage("com.google.android.apps.maps")
                                }
                                if (mapIntent.resolveActivity(context.packageManager) != null) {
                                    context.startActivity(mapIntent)
                                } else {
                                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("geo:$lat,$lng?q=$lat,$lng")))
                                }
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

/**
 * Card Cabang Modern
 */
@Composable
private fun ModernBranchCard(
    item: BranchWithDistance,
    onCardClick: () -> Unit,
    onDirectionsClick: () -> Unit
) {
    val branch = item.branch

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
    ) {
        Column {
            // Gambar Cabang dengan Gradient & Chip Jarak
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.img_kantor),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Shadow Gradient bawah
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                                startY = 50f
                            )
                        )
                )

                // Chip Jarak jika ada
                val jarakText = item.distanceKm?.let {
                    if (it == Double.MAX_VALUE) null
                    else "${"%.1f".format(it)} KM"
                }

                if (jarakText != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.95f))
                            .padding(horizontal = 9.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.NearMe,
                                contentDescription = null,
                                tint = MidnightBlue,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = jarakText,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MidnightBlue
                            )
                        }
                    }
                }

                // Nama Cabang di atas gradient gambar
                Text(
                    text = branch.nama,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                )
            }

            // Info Alamat & Tombol Aksi
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFF64748B),
                        modifier = Modifier
                            .size(16.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = branch.alamat,
                        fontSize = 12.sp,
                        color = Color(0xFF64748B),
                        lineHeight = 17.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onCardClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MidnightBlue
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
                    ) {
                        Text(
                            text = "Lihat Detail",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Button(
                        onClick = onDirectionsClick,
                        modifier = Modifier.height(40.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MidnightBlue,
                            contentColor = SnowWhite
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.NearMe,
                            contentDescription = "Petunjuk Arah",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Rute",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}