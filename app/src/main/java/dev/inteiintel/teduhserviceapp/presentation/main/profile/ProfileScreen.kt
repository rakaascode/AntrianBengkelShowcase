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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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

    LazyColumn {
        item {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

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
                        Text("Profile", fontWeight = FontWeight.Bold)
                    }
                }

                HorizontalDivider(thickness = 1.dp, color = DimGray)

                // Bagian yang connect ke ViewModel
                ProfileScreenStateful(profileViewModel, toDetail)

                LogoutButtonTest(
                    scope = scope,
                    context = context,
                    navController = toDetail,
                    viewModel = authViewModel
                )
            }
        }
    }
}

/**
 * "Stateful" wrapper: nempel ke ViewModel, ambil data, trigger load.
 * Jangan dipanggil langsung dari Preview.
 */
@Composable
fun ProfileScreenStateful(viewModel: ProfileViewModel, navController: NavController) {

    val profileState = viewModel.getProfile.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    ProfileContent(user = profileState, navController = navController)
}

/**
 * "Stateless" UI murni, cuma nerima data lewat parameter.
 * Ini yang aman dipanggil dari Preview.
 */
@Composable
fun ProfileContent(user: UserData?, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        ProfileCard(user, navController = navController)

        Spacer(modifier = Modifier.height(16.dp))

        ActivitySection(navController = navController)

        Spacer(modifier = Modifier.height(16.dp))

        SettingsSection(navController = navController)

        Spacer(modifier = Modifier.height(16.dp))

        SupportAndInfoSection(navController = navController)
    }
}

@Composable
fun ProfileCard(user: UserData?, navController: NavController) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = user?.avatar_url,
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.img_kantor),
                error = painterResource(id = R.drawable.img_kantor),
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = user?.name ?: "Pengguna",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF141B4D)
            )

            Text(
                text = user?.email ?: "-",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate(Screen.EditProfileScreen.route)
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF101C73)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Edit Profile", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun ActivitySection(navController: NavController) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SectionHeader(title = "Aktivitas Layanan")

            Spacer(modifier = Modifier.height(8.dp))

            ClickableRowItem(
                title = "Riwayat Antrean",
                subtitle = "Lihat status antrean selesai & batal",
                onClick = {
                    navController.navigate(Screen.Riyawat.route)
                }
            )

            HorizontalDivider(color = Color(0xFFF0F0F0))

            ClickableRowItem(
                title = "Pengingat Servis",
                subtitle = "Kelola nomor WhatsApp pengingat",
                onClick = {
                    navController.navigate(Screen.Pengingat.route)
                }
            )

            HorizontalDivider(color = Color(0xFFF0F0F0))

            ClickableRowItem(
                title = "Data Kendaraan Tersimpan",
                subtitle = "Kelola data STNK dan motor Anda",
                onClick = {
                    navController.navigate(Screen.DataTersimpan.route)
                }
            )
        }
    }
}

@Composable
fun SettingsSection(navController: NavController) {
    var notifications by remember { mutableStateOf(true) }

    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SectionHeader(title = "Pengaturan & Preferensi")

            Spacer(modifier = Modifier.height(8.dp))

            RowItem(
                title = "Notifikasi Aplikasi",
                trailing = {
                    Switch(
                        checked = notifications,
                        onCheckedChange = { notifications = it }
                    )
                }
            )

            HorizontalDivider(color = Color(0xFFF0F0F0))

            RowItem(
                title = "Bahasa",
                trailing = {
                    Text("Indonesia", color = Color.Gray, fontSize = 14.sp)
                }
            )
        }
    }
}

@Composable
fun SupportAndInfoSection(navController: NavController) {
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SectionHeader(title = "Pusat Bantuan & Lainnya")

            Spacer(modifier = Modifier.height(8.dp))

            ClickableRowItem(
                title = "Daftar Cabang Bengkel",
                subtitle = "Temukan lokasi bengkel Lautan Teduh",
                onClick = {
                    navController.navigate(Screen.DaftarCabang.route)
                }
            )

            HorizontalDivider(color = Color(0xFFF0F0F0))

            ClickableRowItem(
                title = "Kebijakan Privasi",
                subtitle = "Pelajari perlindungan data Anda",
                onClick = {
                    val intent = android.content.Intent(
                        android.content.Intent.ACTION_VIEW,
                        android.net.Uri.parse("https://rakaascode.site/privacy-policy")
                    )
                    context.startActivity(intent)
                }
            )

            HorizontalDivider(color = Color(0xFFF0F0F0))

            RowItem(
                title = "Versi Aplikasi",
                trailing = {
                    Text("v1.0.0", color = Color.Gray, fontSize = 14.sp)
                }
            )
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Color(0xFF101C73)
        )
    }
}

@Composable
fun ClickableRowItem(
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF141B4D)
            )
            if (!subtitle.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun RowItem(
    title: String,
    trailing: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF141B4D)
        )
        trailing()
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

        ProfileContent(
            user = UserData(
                id = 1,
                name = "Budi Santoso",
                email = "budi@example.com",
                avatar_url = "",
                role = "customer",
                created_at = "2025-01-01T00:00:00Z",
                antrian = emptyList()
            ),
            navController = navController
        )
    }
}

@ThemePreviews
@Composable
fun PreviewLocationCard() {
    TeduhServiceAppTheme {
        LocationCard()
    }
}