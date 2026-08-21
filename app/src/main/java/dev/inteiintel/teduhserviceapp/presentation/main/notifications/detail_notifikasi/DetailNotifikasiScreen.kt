package dev.inteiintel.teduhserviceapp.presentation.main.notifications.detail_notifikasi

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import dev.inteiintel.teduhserviceapp.presentation.main.notifications.getTimeOnly
import dev.inteiintel.teduhserviceapp.presentation.main.queues.getDateOnly
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailNotifikasiScreen(
    onBackClick: () -> Unit = {},
    id: Int,
    viewModel: DetailNotifikacationViewModel = hiltViewModel()
) {
    val detailNotification by viewModel
        .getDataDetailNotification
        .collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDetailNotifications(id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F7FB))
    ) {
        // ─── Top App Bar ─────────────────────────────────────────────────────
        // Rule 3 – User Control & Freedom: tombol back selalu jelas & mudah dijangkau
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 2.dp, ambientColor = Color.Black.copy(alpha = 0.06f))
                .background(Color.White)
                .padding(horizontal = 4.dp, vertical = 8.dp)
        ) {
            // Back button (kiri)
            IconButton(
                onClick = { onBackClick() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    modifier = Modifier.size(22.dp),
                    tint = Color(0xFF1A1C1C)
                )
            }

            // Judul (tengah)
            Text(
                text = "Detail Notifikasi",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF111827),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // ─── Content ─────────────────────────────────────────────────────────
        LazyColumn {
            item {
                // Rule 1 – Visibility of Status: loading state yang jelas
                if (detailNotification == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                color = Color(0xFF0B166F),
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Memuat notifikasi…",
                                fontSize = 13.sp,
                                color = Color(0xFF9CA3AF)
                            )
                        }
                    }
                    return@item
                }

                val data = detailNotification!!
                val tipeLower = data.tipe.lowercase()

                // Tentukan warna aksen berdasarkan tipe
                val (accentColor, badgeText) = when {
                    tipeLower.contains("promo") -> Pair(Color(0xFFFF6B35), "PROMO")
                    tipeLower.contains("antrian") || tipeLower.contains("antrean") ->
                        Pair(Color(0xFF3B4FD8), "ANTREAN")
                    else -> Pair(Color(0xFF22C55E), "INFO")
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // ── Hero Icon + Judul + Waktu ─────────────────────────────
                    // Rule 2 – Match Real World: icon sesuai kategori notifikasi
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(58.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF0A1547),
                                            Color(0xFF1E35C0)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when {
                                    tipeLower.contains("promo") -> Icons.Outlined.LocalOffer
                                    tipeLower.contains("antrian") || tipeLower.contains("antrean") ->
                                        Icons.Outlined.Schedule
                                    else -> Icons.Outlined.NotificationsNone
                                },
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            // Category badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(accentColor.copy(alpha = 0.12f))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = badgeText,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = accentColor,
                                    letterSpacing = 0.5.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(5.dp))

                            Text(
                                text = data.judul,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = Color(0xFF111827),
                                lineHeight = 24.sp
                            )

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = getTimeOnly(data.created_at),
                                fontSize = 12.sp,
                                color = Color(0xFF9CA3AF)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // ── Hero Image ────────────────────────────────────────────
                    // Rule 8 – Aesthetic & Minimalist: gambar full-width, rounded, clip bersih
                    val imageUrl = if (data.gambar_url.isNullOrEmpty())
                        "https://i.pravatar.cc/600"
                    else
                        data.gambar_url

                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Gambar notifikasi",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp)
                            .shadow(
                                elevation = 6.dp,
                                shape = RoundedCornerShape(20.dp),
                                ambientColor = Color.Black.copy(alpha = 0.10f)
                            )
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // ── Deskripsi ─────────────────────────────────────────────
                    // Rule 4 – Consistency: typography hierarki jelas
                    Text(
                        text = data.deskripsi,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF111827),
                        lineHeight = 26.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = data.detail,
                        fontSize = 15.sp,
                        color = Color(0xFF4B5563),
                        lineHeight = 26.sp
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // ── Divider tipis ─────────────────────────────────────────
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFFE5E7EB))
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // ── Info Section ──────────────────────────────────────────
                    // Rule 6 – Recognition over Recall: label jelas, ikon kontekstual
                    Text(
                        text = "Informasi",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9CA3AF),
                        letterSpacing = 0.8.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    NotificationInfoRow(
                        icon = Icons.Outlined.CalendarMonth,
                        label = "Waktu",
                        value = getDateOnly(data.created_at),
                        accentColor = accentColor
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    NotificationInfoRow(
                        icon = Icons.Outlined.LocalOffer,
                        label = "Kategori",
                        value = data.tipe,
                        accentColor = accentColor
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    NotificationInfoRow(
                        icon = Icons.Outlined.Storefront,
                        label = "Cabang",
                        value = data.cabang_id ?: "Semua Cabang",
                        accentColor = accentColor
                    )

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

// ─── Info Row Component ──────────────────────────────────────────────────────
// Rule 8 – Aesthetic & Minimalist: komponen reusable, desain bersih
@Composable
fun NotificationInfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    accentColor: Color = Color(0xFF0B166F)
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(accentColor.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color(0xFF9CA3AF),
                letterSpacing = 0.3.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF111827)
            )
        }
    }
}

// ─── Deprecated: kept for source compatibility ───────────────────────────────
@Deprecated("Gunakan NotificationInfoRow")
@Composable
fun NotificationInfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {
    NotificationInfoRow(icon = icon, label = title, value = value)
}

// ─── Util: format datetime ───────────────────────────────────────────────────
@RequiresApi(Build.VERSION_CODES.O)
fun formatDateTime(date: String): String {
    return try {
        val zdt = ZonedDateTime.parse(date)
            .withZoneSameInstant(ZoneId.of("Asia/Jakarta"))
        zdt.format(DateTimeFormatter.ofPattern("dd MMM yyyy • HH:mm"))
    } catch (e: Exception) {
        date
    }
}