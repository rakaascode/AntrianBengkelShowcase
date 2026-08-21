package dev.inteiintel.teduhserviceapp.presentation.main.home.pantau_antrian

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.inteiintel.teduhserviceapp.data.model.RingkasanCabangItem
import dev.inteiintel.teduhserviceapp.presentation.main.home.RingkasanHomeViewModel
import dev.inteiintel.teduhserviceapp.ui.theme.DarkSlate
import dev.inteiintel.teduhserviceapp.ui.theme.DimGray
import dev.inteiintel.teduhserviceapp.ui.theme.MidnightBlue
import dev.inteiintel.teduhserviceapp.ui.theme.SnowWhite

/**
 * Halaman Pantau Antrian — menampilkan status antrian realtime dari semua cabang.
 *
 * User dapat melihat nomor yang sedang dipanggil, estimasi waktu,
 * dan sisa antrian untuk setiap cabang bengkel secara langsung.
 *
 * @param navController Controller navigasi untuk tombol kembali.
 * @param viewModel ViewModel yang mengambil data ringkasan semua cabang.
 */
@Composable
fun PantauAntrianScreen(
    navController: NavController,
    viewModel: RingkasanHomeViewModel = hiltViewModel()
) {
    val allCabang by viewModel.allCabang.collectAsState()
    val loading by viewModel.loading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getAllBranches()
    }

    Column(modifier = Modifier.fillMaxSize().background(SnowWhite)) {

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
                    text = "Pantau Antrian",
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

            // Tombol refresh di kanan
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 4.dp)
            ) {
                IconButton(onClick = { viewModel.getAllBranches() }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        modifier = Modifier.size(20.dp),
                        tint = MidnightBlue
                    )
                }
            }
        }

        HorizontalDivider(thickness = 1.dp, color = Color(0xFFE8ECF4))

        // ─── Konten ──────────────────────────────────────────────────────────
        when {
            loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = MidnightBlue)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Memuat data antrian...",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            allCabang.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🏪", fontSize = 40.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Tidak ada data cabang",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "Semua Cabang • ${allCabang.size} lokasi",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                    items(allCabang) { cabang ->
                        CabangAntrianCard(cabang = cabang)
                    }
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }
            }
        }
    }
}

/**
 * Card satu cabang di halaman Pantau Antrian.
 *
 * Menampilkan:
 * - Nama cabang & status aktif/tutup
 * - Nomor antrian yang sedang dipanggil
 * - Progress bar sisa antrian
 * - Estimasi waktu & jumlah sisa antrian
 */
@Composable
fun CabangAntrianCard(cabang: RingkasanCabangItem) {
    val isAktif = cabang.sisaAntrian > 0 || cabang.nomorDipanggil != null

    val statusColor by animateColorAsState(
        targetValue = if (isAktif) Color(0xFF16A34A) else Color.Gray,
        animationSpec = tween(300),
        label = "statusColor"
    )

    val nomorText = when {
        cabang.nomorDipanggil != null && cabang.nomorDipanggil != 0 -> "A%03d".format(cabang.nomorDipanggil)
        else -> "—"
    }

    // Estimasi progress: maks 30 antrian
    val progress = (1f - (cabang.sisaAntrian.toFloat() / 30f)).coerceIn(0f, 1f)

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Header: nama cabang + badge status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = cabang.namaCabang,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = DarkSlate,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Badge status
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(statusColor.copy(alpha = 0.1f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(statusColor)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (isAktif) "Aktif" else "Sepi",
                        fontSize = 11.sp,
                        color = statusColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nomor dipanggil
            Row(verticalAlignment = Alignment.Bottom) {
                Column {
                    Text(
                        text = "Nomor Dipanggil",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = nomorText,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MidnightBlue
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Sisa antrian (kanan)
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Sisa Antrian",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "${cabang.sisaAntrian}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (cabang.sisaAntrian > 10) Color(0xFFEA580C) else MidnightBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(50)),
                color = MidnightBlue,
                trackColor = Color(0xFFE8ECF4)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Estimasi jam
            Text(
                text = "Estimasi: ${cabang.estimasiJam.ifBlank { "—" }}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}
