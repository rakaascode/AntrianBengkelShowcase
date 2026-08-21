package dev.inteiintel.teduhserviceapp.presentation.main.home.panduan

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import dev.inteiintel.teduhserviceapp.ui.theme.DarkSlate
import dev.inteiintel.teduhserviceapp.ui.theme.DimGray
import dev.inteiintel.teduhserviceapp.ui.theme.MidnightBlue
import dev.inteiintel.teduhserviceapp.ui.theme.SnowWhite

data class TipPerawatan(
    val emoji: String,
    val judul: String,
    val deskripsi: String,
    val interval: String
)

private val daftarTips = listOf(
    TipPerawatan(
        emoji = "🛢️",
        judul = "Ganti Oli Mesin",
        deskripsi = "Oli mesin berfungsi melumasi komponen internal agar tidak cepat aus. Gunakan oli sesuai spesifikasi motor Anda dan jangan tunda penggantian.",
        interval = "Setiap 2.000–3.000 km atau 2–3 bulan"
    ),
    TipPerawatan(
        emoji = "🔋",
        judul = "Periksa Aki",
        deskripsi = "Aki lemah bisa menyebabkan motor sulit distarter. Cek tegangan aki secara berkala, terutama sebelum musim hujan.",
        interval = "Setiap 6 bulan sekali"
    ),
    TipPerawatan(
        emoji = "🔧",
        judul = "Setel Rantai / CVT",
        deskripsi = "Rantai yang kendur atau CVT yang aus dapat mengurangi performa dan membahayakan keselamatan. Periksa keregangan rantai secara rutin.",
        interval = "Setiap 5.000 km"
    ),
    TipPerawatan(
        emoji = "💨",
        judul = "Filter Udara",
        deskripsi = "Filter udara yang kotor membuat pembakaran tidak sempurna dan boros bahan bakar. Bersihkan atau ganti sesuai kondisi.",
        interval = "Setiap 5.000–8.000 km"
    ),
    TipPerawatan(
        emoji = "🛑",
        judul = "Rem & Kampas Rem",
        deskripsi = "Kampas rem yang tipis sangat berbahaya. Segera ganti jika terdengar suara decit atau jarak pengereman terasa lebih jauh dari biasanya.",
        interval = "Setiap 10.000–15.000 km atau sesuai keausan"
    ),
    TipPerawatan(
        emoji = "🔌",
        judul = "Busi",
        deskripsi = "Busi aus menyebabkan mesin susah hidup dan boros bensin. Pilih busi sesuai rekomendasi pabrikan motor Anda.",
        interval = "Setiap 8.000–12.000 km"
    ),
    TipPerawatan(
        emoji = "🌡️",
        judul = "Cairan Radiator (Motor Liquid Cooled)",
        deskripsi = "Khusus motor berpendingin cairan. Pastikan level coolant selalu di batas normal agar mesin tidak overheat.",
        interval = "Setiap 20.000 km atau 1 tahun"
    ),
    TipPerawatan(
        emoji = "🔩",
        judul = "Servis Berkala",
        deskripsi = "Servis berkala di bengkel resmi memastikan seluruh komponen diperiksa menyeluruh oleh mekanik berpengalaman dan menggunakan suku cadang asli.",
        interval = "Setiap 6 bulan atau 5.000 km"
    )
)

/**
 * Halaman Panduan — berisi tips perawatan kendaraan bermotor.
 *
 * Menampilkan daftar tips perawatan dalam bentuk card yang informatif
 * dengan interval perawatan yang direkomendasikan.
 *
 * @param navController Controller navigasi untuk tombol kembali.
 */
@Composable
fun PanduanScreen(navController: NavController) {

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
                    text = "Panduan Perawatan",
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

        // ─── Konten ──────────────────────────────────────────────────────────
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tips Perawatan Kendaraan",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkSlate
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Rawat kendaraan Anda secara rutin agar tetap prima dan aman di jalan.",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            itemsIndexed(daftarTips) { index, tip ->
                TipCard(index = index + 1, tip = tip)
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun TipCard(index: Int, tip: TipPerawatan) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Nomor urut & emoji
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MidnightBlue.copy(alpha = 0.08f))
                ) {
                    Text(
                        text = tip.emoji,
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "#$index",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Konten tips
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tip.judul,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkSlate
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = tip.deskripsi,
                    fontSize = 13.sp,
                    color = Color(0xFF4B5563),
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Interval badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MidnightBlue.copy(alpha = 0.07f))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(text = "⏱️", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = tip.interval,
                        fontSize = 11.sp,
                        color = MidnightBlue,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
