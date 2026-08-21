package dev.inteiintel.teduhserviceapp.presentation.main.notifications

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import dev.inteiintel.teduhserviceapp.R
import dev.inteiintel.teduhserviceapp.data.model.NotificationData
import dev.inteiintel.teduhserviceapp.utils.navigation.Screen
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationsScreen(
    viewModel: NotificationsViewModel = hiltViewModel(),
    navController: NavController,
) {
    val notifications by viewModel.getNotificationData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val unreadCount = notifications.count { !it.isRead }

    var selectedCategory by remember { mutableStateOf("Semua") }
    val categories = listOf("Semua", "Promo", "Antrean", "Info")

    val filteredNotifications = when (selectedCategory) {
        "Promo" -> notifications.filter { it.tipe?.lowercase()?.contains("promo") == true }
        "Antrean" -> notifications.filter {
            it.tipe?.lowercase()?.contains("antrian") == true ||
                    it.tipe?.lowercase()?.contains("antrean") == true
        }
        "Info" -> notifications.filter {
            it.tipe?.lowercase()?.contains("info") == true ||
                    it.tipe?.lowercase()?.contains("pengumuman") == true
        }
        else -> notifications
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    viewModel.refresh()
                    viewModel.startPolling()
                }
                Lifecycle.Event.ON_PAUSE -> viewModel.stopPolling()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val pullState = rememberPullToRefreshState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F3FB))
    ) {
        // ─── Header ─────────────────────────────────────────────────────────
        // Rule 1 – Visibility of System Status: unread count selalu terlihat di header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0A1547),
                            Color(0xFF122180),
                            Color(0xFF1E35C0)
                        )
                    )
                )
                .padding(top = 32.dp, bottom = 24.dp, start = 20.dp, end = 20.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        // Badge di atas judul supaya user langsung tahu ada pesan baru
                        if (unreadCount > 0) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color(0xFFFF3B30).copy(alpha = 0.18f))
                                    .padding(horizontal = 10.dp, vertical = 3.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${if (unreadCount > 99) "99+" else unreadCount} pesan baru",
                                    fontSize = 10.sp,
                                    color = Color(0xFFFF8A80),
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = 0.3.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        Text(
                            text = "Pusat Notifikasi",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp,
                            color = Color.White,
                            letterSpacing = (-0.3).sp
                        )

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = "Promo & info antrean servis Anda",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.65f),
                            letterSpacing = 0.1.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        // ─── Category Filter Chips ───────────────────────────────────────────
        // Rule 4 – Consistency & Standards + Rule 7 – Flexibility: chip mudah diklik
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { category ->
                val isSelected = selectedCategory == category

                // Rule 6 – Recognition over Recall: warna animasi memberikan feedback langsung
                val bgColor by animateColorAsState(
                    targetValue = if (isSelected) Color(0xFF0F1A65) else Color.White,
                    animationSpec = tween(durationMillis = 200),
                    label = "chip_bg"
                )
                val textColor by animateColorAsState(
                    targetValue = if (isSelected) Color.White else Color(0xFF6B7280),
                    animationSpec = tween(durationMillis = 200),
                    label = "chip_text"
                )

                Box(
                    modifier = Modifier
                        .shadow(
                            elevation = if (isSelected) 4.dp else 1.dp,
                            shape = RoundedCornerShape(22.dp),
                            ambientColor = Color(0xFF0F1A65).copy(alpha = 0.15f)
                        )
                        .clip(RoundedCornerShape(22.dp))
                        .background(bgColor)
                        .clickable { selectedCategory = category }
                        .padding(horizontal = 18.dp, vertical = 9.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = textColor,
                        letterSpacing = 0.2.sp
                    )
                }
            }
        }

        // ─── Content + Pull-to-Refresh ───────────────────────────────────────
        // Rule 3 – User Control & Freedom: pull-to-refresh selalu tersedia
        PullToRefreshBox(
            isRefreshing = isLoading,
            onRefresh = { viewModel.refresh() },
            state = pullState,
            modifier = Modifier.fillMaxSize()
        ) {
            if (filteredNotifications.isEmpty() && !isLoading) {
                // Rule 9 – Help Users Recognize, Diagnose, and Recover: empty state informatif
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 72.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.img_not_found),
                        contentDescription = "Tidak ada notifikasi",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(160.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Belum Ada Notifikasi",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF141B4D)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (selectedCategory == "Semua")
                            "Notifikasi promo & panggilan antrean\nakan muncul di sini"
                        else
                            "Tidak ada notifikasi dalam kategori\n\"$selectedCategory\"",
                        fontSize = 13.sp,
                        color = Color(0xFF9CA3AF),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(horizontal = 40.dp)
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 4.dp,
                        bottom = 32.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = filteredNotifications,
                        key = { it.id }
                    ) { item ->
                        NotificationCard(
                            item = item,
                            onClick = {
                                viewModel.markAsRead(item.id)
                                navController.navigate(
                                    Screen.DetailNotification.createRoute(item.id)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

// ─── Notification Card ───────────────────────────────────────────────────────
// Rule 5 – Error Prevention + Rule 8 – Aesthetic & Minimalist Design
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationCard(
    item: NotificationData,
    onClick: () -> Unit
) {
    val isRead = item.isRead
    val tipeLower = item.tipe?.lowercase() ?: ""

    // Rule 6 – Recognition over Recall: ikon & warna konsisten per kategori
    val config = when {
        tipeLower.contains("promo") -> NotifConfig(
            iconBg = Color(0xFFFFF0E6),
            iconTint = Color(0xFFE55A00),
            icon = Icons.Default.LocalOffer,
            badgeText = "PROMO",
            badgeColor = Color(0xFFFF6B35),
            accentColor = Color(0xFFFF6B35)
        )
        tipeLower.contains("antrian") || tipeLower.contains("antrean") -> NotifConfig(
            iconBg = Color(0xFFECEEFD),
            iconTint = Color(0xFF3B4FD8),
            icon = Icons.Default.Schedule,
            badgeText = "ANTREAN",
            badgeColor = Color(0xFF3B4FD8),
            accentColor = Color(0xFF3B4FD8)
        )
        else -> NotifConfig(
            iconBg = Color(0xFFE8F5E9),
            iconTint = Color(0xFF1B8C4A),
            icon = Icons.Default.Notifications,
            badgeText = "INFO",
            badgeColor = Color(0xFF22C55E),
            accentColor = Color(0xFF22C55E)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isRead) 1.dp else 4.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = config.accentColor.copy(alpha = 0.08f)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(if (isRead) Color(0xFFF9FAFD) else Color.White)
            .clickable { onClick() }
    ) {
        // Rule 1 – Visibility of Status: left accent bar hanya pada unread
        if (!isRead) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(80.dp)
                    .align(Alignment.CenterStart)
                    .clip(RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                config.accentColor,
                                config.accentColor.copy(alpha = 0.3f)
                            )
                        )
                    )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = if (isRead) 14.dp else 18.dp,
                    end = 14.dp,
                    top = 14.dp,
                    bottom = 14.dp
                ),
            verticalAlignment = Alignment.Top
        ) {
            // ── Icon Bubble ──
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(config.iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = config.icon,
                    contentDescription = null,
                    tint = config.iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Badge + timestamp + unread dot
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(config.badgeColor.copy(alpha = 0.10f))
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = config.badgeText,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = config.badgeColor,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = getTimeOnly(item.created_at ?: ""),
                            fontSize = 11.sp,
                            color = Color(0xFFB0B8CC)
                        )
                        if (!isRead) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFF3B30))
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(5.dp))

                // Judul
                Text(
                    text = item.judul ?: "Informasi Penting",
                    fontWeight = if (isRead) FontWeight.SemiBold else FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (isRead) Color(0xFF374151) else Color(0xFF111827),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(3.dp))

                // Deskripsi singkat
                Text(
                    text = item.deskripsi ?: "",
                    fontSize = 12.sp,
                    color = if (isRead) Color(0xFF9CA3AF) else Color(0xFF6B7280),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 17.sp
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFFD1D5DB),
                modifier = Modifier
                    .size(18.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

// ─── Config data class ───────────────────────────────────────────────────────
data class NotifConfig(
    val iconBg: Color,
    val iconTint: Color,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val badgeText: String,
    val badgeColor: Color,
    val accentColor: Color
)

// ─── Deprecated Quintuple — kept for source compatibility ────────────────────
@Deprecated("Gunakan NotifConfig")
data class Quintuple<A, B, C, D, E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
)

// ─── Util: format waktu ──────────────────────────────────────────────────────
@RequiresApi(Build.VERSION_CODES.O)
fun getTimeOnly(date: String): String {
    return try {
        val zdt = ZonedDateTime.parse(date)
            .withZoneSameInstant(ZoneId.of("Asia/Jakarta"))
        zdt.format(DateTimeFormatter.ofPattern("HH:mm"))
    } catch (e: Exception) {
        "Baru saja"
    }
}