package dev.inteiintel.teduhserviceapp.presentation.main.home.detail_cabang

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import dev.inteiintel.teduhserviceapp.R
import dev.inteiintel.teduhserviceapp.data.model.Branch
import dev.inteiintel.teduhserviceapp.data.model.RingkasanCabangItem
import dev.inteiintel.teduhserviceapp.ui.theme.DarkSlate
import dev.inteiintel.teduhserviceapp.ui.theme.MidnightBlue
import dev.inteiintel.teduhserviceapp.ui.theme.SnowWhite
import dev.inteiintel.teduhserviceapp.utils.navigation.Screen
import java.util.Locale

/**
 * Layar Detail Cabang yang telah didesain ulang dengan gaya modern.
 *
 * Menampilkan:
 * - Header image dengan tombol kembali overlay.
 * - Card identitas cabang, status buka/tutup, dan quick action (Maps & Telepon).
 * - Card Live Antrian Realtime (nomor dipanggil, sisa antrian, progress bar).
 * - Jadwal Jam Operasional & Informasi Kontak.
 * - Floating Bottom Action Bar untuk langsung mengambil antrian.
 */
@Composable
fun DetailCabangScreen(
    detailCabangViewModel: DetailCabangViewModel = hiltViewModel(),
    navController: NavController,
    branchId: Int
) {
    val dataBranch by detailCabangViewModel.dataBranch.collectAsState()
    val ringkasanCabang by detailCabangViewModel.ringkasanCabang.collectAsState()

    LaunchedEffect(branchId) {
        detailCabangViewModel.loadDataBranch(branchId)
        detailCabangViewModel.loadRingkasanCabang(branchId)
    }

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SnowWhite)
    ) {
        if (dataBranch == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = MidnightBlue)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Memuat detail cabang...",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }
        } else {
            val branch = dataBranch!!

            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    // 1. Header Image with Overlay Back Button & Badge
                    item {
                        HeaderBranchImage(
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    // 2. Info Card: Nama, Alamat & Quick Actions
                    item {
                        BranchInfoSection(
                            branch = branch,
                            onGetDirections = {
                                val lat = branch.latitude
                                val lng = branch.longitude
                                val gmmIntentUri = Uri.parse("google.navigation:q=$lat,$lng")
                                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                                    setPackage("com.google.android.apps.maps")
                                }
                                if (mapIntent.resolveActivity(context.packageManager) != null) {
                                    context.startActivity(mapIntent)
                                } else {
                                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("geo:$lat,$lng?q=$lat,$lng")))
                                }
                            },
                            onCall = {
                                if (branch.no_telp.isNotBlank()) {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${branch.no_telp}"))
                                    context.startActivity(intent)
                                }
                            }
                        )
                    }

                    // 3. Live Queue Card (jika tersedia data antrian)
                    item {
                        LiveQueueCard(ringkasan = ringkasanCabang)
                    }

                    // 4. Jam Operasional Card
                    item {
                        OperationalHoursCard()
                    }

                    // 5. Kontak & Layanan Card
                    item {
                        ContactInfoCard(branch = branch)
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }

                // 6. Sticky Bottom Action Bar
                BottomActionBar(
                    onAmbilAntrian = {
                        navController.navigate(Screen.AmbilAntreanFromBeranda.route)
                    }
                )
            }
        }
    }
}

/**
 * Header Foto Cabang dengan tombol Back dan status buka
 */
@Composable
private fun HeaderBranchImage(
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
    ) {
        Image(
            painter = painterResource(R.drawable.img_kantor),
            contentDescription = "Foto Cabang",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Overlay Gradient tipis di atas untuk kontras tombol back
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .background(
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent)
                    )
                )
        )

        // Back Button melayang
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(top = 8.dp, start = 8.dp)
                .align(Alignment.TopStart)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.85f))
                .size(38.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Kembali",
                tint = DarkSlate,
                modifier = Modifier.size(20.dp)
            )
        }

        // Chip Status Buka di pojok kanan bawah foto
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF16A34A).copy(alpha = 0.9f))
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Buka Hari Ini",
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * Section Info Cabang: Judul, Alamat, dan Tombol Quick Action (Arah & Telp)
 */
@Composable
private fun BranchInfoSection(
    branch: Branch,
    onGetDirections: () -> Unit,
    onCall: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = branch.nama.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                },
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = DarkSlate
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MidnightBlue,
                    modifier = Modifier
                        .size(18.dp)
                        .padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = branch.alamat,
                    fontSize = 13.sp,
                    color = Color(0xFF64748B),
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Actions Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onGetDirections,
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MidnightBlue),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
                ) {
                    Icon(
                        imageVector = Icons.Default.NearMe,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Petunjuk Arah",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                OutlinedButton(
                    onClick = onCall,
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MidnightBlue),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Hubungi",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

/**
 * Card Live Antrian di cabang tersebut
 */
@Composable
private fun LiveQueueCard(
    ringkasan: RingkasanCabangItem?
) {
    if (ringkasan == null) return

    val nomorText = when {
        ringkasan.nomorDipanggil != null && ringkasan.nomorDipanggil != 0 -> "A%03d".format(ringkasan.nomorDipanggil)
        else -> "—"
    }
    val progress = (1f - (ringkasan.sisaAntrian.toFloat() / 30f)).coerceIn(0f, 1f)

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4FF)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2563EB))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ANTRIAN SAAT INI",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E40AF),
                        letterSpacing = 0.5.sp
                    )
                }

                Text(
                    text = "Estimasi: ${ringkasan.estimasiJam.ifBlank { "Normal" }}",
                    fontSize = 12.sp,
                    color = Color(0xFF475569),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Sedang Dilayani",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                    Text(
                        text = nomorText,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MidnightBlue
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Sisa Menunggu",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                    Text(
                        text = "${ringkasan.sisaAntrian} Motor",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (ringkasan.sisaAntrian > 10) Color(0xFFEA580C) else MidnightBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(50)),
                color = MidnightBlue,
                trackColor = Color(0xFFCBD5E1)
            )
        }
    }
}

/**
 * Card Jam Operasional Cabang
 */
@Composable
private fun OperationalHoursCard() {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = MidnightBlue,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Jam Operasional",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkSlate
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            val schedule = listOf(
                Pair("Senin – Jumat", "09:00 – 17:00"),
                Pair("Sabtu", "09:00 – 17:00"),
                Pair("Minggu / Libur Nasional", "Tutup")
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                schedule.forEach { (hari, jam) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = hari,
                            fontSize = 13.sp,
                            color = Color(0xFF475569)
                        )
                        Text(
                            text = jam,
                            fontSize = 13.sp,
                            fontWeight = if (jam == "Tutup") FontWeight.Medium else FontWeight.SemiBold,
                            color = if (jam == "Tutup") Color(0xFFEF4444) else DarkSlate
                        )
                    }
                }
            }
        }
    }
}

/**
 * Card Informasi Kontak & Email
 */
@Composable
private fun ContactInfoCard(branch: Branch) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Storefront,
                    contentDescription = null,
                    tint = MidnightBlue,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Informasi Tambahan",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkSlate
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = null,
                    tint = Color(0xFF64748B),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Telepon Cabang", fontSize = 11.sp, color = Color.Gray)
                    Text(
                        text = branch.no_telp.ifBlank { "Tidak tersedia" },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = DarkSlate
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = Color(0xFF64748B),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Email CS", fontSize = 11.sp, color = Color.Gray)
                    Text(
                        text = "teduhservice@gmail.com",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = DarkSlate
                    )
                }
            }
        }
    }
}

/**
 * Sticky Bottom Bar dengan tombol aksi Ambil Antrian
 */
@Composable
private fun BottomActionBar(
    onAmbilAntrian: () -> Unit
) {
    Surface(
        color = Color.White,
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Button(
                onClick = onAmbilAntrian,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MidnightBlue,
                    contentColor = SnowWhite
                )
            ) {
                Icon(
                    imageVector = Icons.Default.ConfirmationNumber,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Ambil Antrian di Cabang Ini",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}