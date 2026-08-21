package dev.inteiintel.teduhserviceapp.presentation.main.profile

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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.Motorcycle
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import kotlinx.coroutines.launch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import dev.inteiintel.teduhserviceapp.presentation.main.LogoutButtonTest
import dev.inteiintel.teduhserviceapp.R
import dev.inteiintel.teduhserviceapp.data.model.UserData
import dev.inteiintel.teduhserviceapp.presentation.auth.AuthViewModel
import dev.inteiintel.teduhserviceapp.ui.theme.DarkSlate
import dev.inteiintel.teduhserviceapp.ui.theme.DimGray
import dev.inteiintel.teduhserviceapp.ui.theme.SnowWhite
import dev.inteiintel.teduhserviceapp.ui.theme.TeduhServiceAppTheme
import dev.inteiintel.teduhserviceapp.ui.theme.ThemePreviews
import dev.inteiintel.teduhserviceapp.utils.navigation.Screen


@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    toDetail: NavController,
    navController: NavHostController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userProfile by profileViewModel.getProfile.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FD))
    ) {
        // ─── Header Top Bar (Konsisten dengan Top Bar App) ───────────────────
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
                    text = "Akun Saya",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DarkSlate
                )
            }
        }

        HorizontalDivider(thickness = 1.dp, color = Color(0xFFE8ECF4))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                HeroProfileCard(user = userProfile, navController = toDetail)
            }

            item {
                QuickStatsCard(user = userProfile, navController = toDetail)
            }

            item {
                ServicesMenuSection(navController = toDetail)
            }

            item {
                GeneralSettingsSection(navController = toDetail)
            }

            item {
                HelpAndInfoSection(navController = toDetail)
            }

            item {
                LogoutButtonSection(
                    onLogout = {
                        scope.launch {
                            authViewModel.deleteToken(context)
                            toDetail.navigate(Screen.Auth.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun HeroProfileCard(user: UserData?, navController: NavController) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF1F5F9))
                        .border(2.dp, Color(0xFFE2E8F0), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = user?.avatar_url,
                        contentDescription = "Avatar",
                        placeholder = painterResource(id = R.drawable.img_kantor),
                        error = painterResource(id = R.drawable.img_kantor),
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = user?.name ?: "Pengguna Lautan Teduh",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkSlate,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = user?.email ?: "Memuat data...",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFFF9800).copy(alpha = 0.12f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (user?.role == "admin") "ADMIN CABANG" else "MEMBER RESMI",
                            color = Color(0xFFD97706),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedButton(
                onClick = { navController.navigate(Screen.EditProfileScreen.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1))
            ) {
                Text(
                    text = "Edit Profil",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkSlate
                )
            }
        }
    }
}

@Composable
fun QuickStatsCard(user: UserData?, navController: NavController) {
    val totalAntrean = user?.antrian?.size ?: 0
    val antreanAktif = user?.antrian?.count { it.status.lowercase() == "menunggu" || it.status.lowercase() == "dipanggil" } ?: 0

    Card(
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem(
                title = "Total Servis",
                value = "$totalAntrean",
                subtitle = "Riwayat",
                onClick = { navController.navigate(Screen.Riyawat.route) }
            )

            Box(
                modifier = Modifier
                    .height(36.dp)
                    .width(1.dp)
                    .background(Color(0xFFE8EBF2))
            )

            StatItem(
                title = "Antrean Aktif",
                value = "$antreanAktif",
                subtitle = "Berjalan",
                onClick = { navController.navigate(Screen.Queues.route) }
            )

            Box(
                modifier = Modifier
                    .height(36.dp)
                    .width(1.dp)
                    .background(Color(0xFFE8EBF2))
            )

            StatItem(
                title = "Status WA",
                value = if (!user?.no_wa.isNullOrBlank()) "Aktif" else "Atur",
                subtitle = "Pengingat",
                onClick = { navController.navigate(Screen.Pengingat.route) }
            )
        }
    }
}

@Composable
fun StatItem(
    title: String,
    value: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = value,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF101C73)
        )
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF5A6275)
        )
        Text(
            text = subtitle,
            fontSize = 10.sp,
            color = Color(0xFF9EA6BA)
        )
    }
}

@Composable
fun ServicesMenuSection(navController: NavController) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "Layanan & Aktivitas",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color(0xFF141B4D)
            )

            Spacer(modifier = Modifier.height(14.dp))

            ModernMenuRow(
                icon = Icons.Default.DateRange,
                iconBg = Color(0xFFE8EAF6),
                iconTint = Color(0xFF3F51B5),
                title = "Riwayat Servis Bengkel",
                subtitle = "Lihat status servis yang telah selesai & dibatalkan",
                onClick = { navController.navigate(Screen.Riyawat.route) }
            )

            HorizontalDivider(color = Color(0xFFF3F4F8), modifier = Modifier.padding(vertical = 4.dp))

            ModernMenuRow(
                icon = Icons.Default.LockClock,
                iconBg = Color(0xFFE8F5E9),
                iconTint = Color(0xFF2E7D32),
                title = "WhatsApp Reminder",
                subtitle = "Nomor WhatsApp aktif untuk pengingat servis",
                onClick = { navController.navigate(Screen.Pengingat.route) }
            )

            HorizontalDivider(color = Color(0xFFF3F4F8), modifier = Modifier.padding(vertical = 4.dp))

            ModernMenuRow(
                icon = Icons.Default.Motorcycle,
                iconBg = Color(0xFFFFF3E0),
                iconTint = Color(0xFFE65100),
                title = "Data Kendaraan Tersimpan",
                subtitle = "Daftar motor dan nomor rangka STNK",
                onClick = { navController.navigate(Screen.DataTersimpan.route) }
            )
        }
    }
}

@Composable
fun GeneralSettingsSection(navController: NavController) {
    var notificationsEnabled by remember { mutableStateOf(true) }

    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "Pengaturan Aplikasi",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color(0xFF141B4D)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFEDE7F6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = Color(0xFF673AB7),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Notifikasi Push",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF141B4D)
                        )
                        Text(
                            text = "Info update antrean & pengumuman promo",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it }
                )
            }
        }
    }
}

@Composable
fun HelpAndInfoSection(navController: NavController) {
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "Pusat Bantuan & Info",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color(0xFF141B4D)
            )

            Spacer(modifier = Modifier.height(14.dp))

            ModernMenuRow(
                icon = Icons.Default.Place,
                iconBg = Color(0xFFE0F2F1),
                iconTint = Color(0xFF00796B),
                title = "Lokasi Bengkel & Jam Buka",
                subtitle = "Temukan dealer & bengkel resmi terdekat",
                onClick = { navController.navigate(Screen.DaftarCabang.route) }
            )

            HorizontalDivider(color = Color(0xFFF3F4F8), modifier = Modifier.padding(vertical = 4.dp))

            ModernMenuRow(
                icon = Icons.Default.Star,
                iconBg = Color(0xFFFCE4EC),
                iconTint = Color(0xFFC2185B),
                title = "Kebijakan Privasi",
                subtitle = "Perlindungan data & privasi pengguna",
                onClick = {
                    val intent = android.content.Intent(
                        android.content.Intent.ACTION_VIEW,
                        android.net.Uri.parse("https://rakaascode.site/privacy-policy")
                    )
                    context.startActivity(intent)
                }
            )

            HorizontalDivider(color = Color(0xFFF3F4F8), modifier = Modifier.padding(vertical = 4.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Versi Aplikasi",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                Text(
                    text = "v1.3 (Build 2026)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF101C73)
                )
            }
        }
    }
}

@Composable
fun ModernMenuRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF141B4D)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = Color(0xFF8B92A4),
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color(0xFFC0C5D3),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun LogoutButtonSection(onLogout: () -> Unit) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F1)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD5D5)),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onLogout() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Keluar dari Akun",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD32F2F)
            )
        }
    }
}


@Composable
fun LocationCard() {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Lautan Teduh Teluk",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "Jl. Ikan Tenggiri No.24, Pesawahan, Kec. Telukbetung Selatan, Kota Bandar Lampung, Lampung",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "1.2 km",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Image(
                painter = painterResource(id = R.drawable.img_kantor),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.Yellow,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "4.8",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Button(
                    onClick = {},
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1A2B6D)
                    ),
                    modifier = Modifier.size(100.dp, 40.dp)
                ) {
                    Text("Lihat Detail", color = Color.White)
                }
            }
        }
    }
}


@ThemePreviews
@Composable
fun PreviewProfileScreen() {
    TeduhServiceAppTheme {
        val navController = rememberNavController()

        HeroProfileCard(
            user = UserData(
                id = 1,
                name = "Budi Santoso",
                email = "budi@example.com",
                avatar_url = "",
                role = "user",
                created_at = "2026-01-01T00:00:00Z",
                antrian = emptyList()
            ),
            navController = navController
        )
    }
}