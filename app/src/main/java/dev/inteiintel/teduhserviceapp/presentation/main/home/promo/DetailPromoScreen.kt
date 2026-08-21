package dev.inteiintel.teduhserviceapp.presentation.main.home.promo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.inteiintel.teduhserviceapp.R
import dev.inteiintel.teduhserviceapp.ui.theme.DarkSlate
import dev.inteiintel.teduhserviceapp.ui.theme.MidnightBlue
import dev.inteiintel.teduhserviceapp.ui.theme.SnowWhite
import dev.inteiintel.teduhserviceapp.utils.navigation.Screen

/**
 * Halaman Detail Promo — menampilkan rincian promo, syarat & ketentuan,
 * masa berlaku promo, dan tombol call to action untuk langsung ambil antrian.
 */
@Composable
fun DetailPromoScreen(
    navController: NavController
) {
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
                    text = "Detail Promo",
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

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(6.dp))

                // Banner Promo Image
                Card(
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_promo),
                        contentDescription = "Promo Banner",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            item {
                // Judul Promo & Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Promo Spesial Servis Lengkap",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkSlate,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFE0F2FE))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Diskon 30%",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0284C7)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Dapatkan diskon paket servis lengkap dan ganti oli mesin khusus untuk pendaftaran antrian online melalui aplikasi Teduh Service.",
                    fontSize = 13.sp,
                    color = Color(0xFF4B5563),
                    lineHeight = 20.sp
                )
            }

            item {
                // Info Box: Periode & Lokasi
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MidnightBlue
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Periode: Berlaku hingga akhir bulan",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = DarkSlate
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MidnightBlue
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Berlaku di: Seluruh cabang Teduh Service",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = DarkSlate
                            )
                        }
                    }
                }
            }

            item {
                // Syarat dan Ketentuan
                Text(
                    text = "Syarat & Ketentuan",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkSlate
                )

                Spacer(modifier = Modifier.height(8.dp))

                val terms = listOf(
                    "Promo hanya berlaku untuk pengambilan antrian melalui aplikasi.",
                    "Berlaku untuk semua jenis sepeda motor matic dan bebek.",
                    "Tidak dapat digabungkan dengan voucher diskon lainnya.",
                    "Tunjukkan kode antrian kepada petugas kasir saat pembayaran."
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    terms.forEach { term ->
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF16A34A),
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = term,
                                fontSize = 12.sp,
                                color = Color(0xFF4B5563),
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }

        // Bottom CTA Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SnowWhite)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Button(
                onClick = {
                    navController.navigate(Screen.AmbilAntreanFromBeranda.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
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
                    text = "Ambil Antrian Sekarang",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
