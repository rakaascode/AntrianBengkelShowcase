package dev.inteiintel.teduhserviceapp.presentation.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import dev.inteiintel.teduhserviceapp.data.local.TokenManager
import dev.inteiintel.teduhserviceapp.ui.theme.DarkOrange
import dev.inteiintel.teduhserviceapp.ui.theme.DarkSlate
import dev.inteiintel.teduhserviceapp.ui.theme.DimGray
import dev.inteiintel.teduhserviceapp.ui.theme.MidnightBlue
import dev.inteiintel.teduhserviceapp.utils.navigation.Screen
import kotlinx.coroutines.launch

data class PermissionItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconBg: Color,
    val iconTint: Color,
    val permissionKey: String?
)

/**
 * Layar pengenalan izin aplikasi (Notifikasi, Kamera, Lokasi) yang tampil
 * saat pertama kali aplikasi dibuka setelah onboarding.
 */
@Composable
fun PermissionRequestScreen(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val tokenManager = remember { TokenManager(context) }

    fun checkIsGranted(permission: String?): Boolean {
        if (permission == null) return true
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    var isNotificationGranted by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                checkIsGranted(Manifest.permission.POST_NOTIFICATIONS)
            } else true
        )
    }

    var isCameraGranted by remember {
        mutableStateOf(checkIsGranted(Manifest.permission.CAMERA))
    }

    var isLocationGranted by remember {
        mutableStateOf(
            checkIsGranted(Manifest.permission.ACCESS_FINE_LOCATION) ||
                    checkIsGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
        )
    }

    fun proceedToNextScreen() {
        coroutineScope.launch {
            tokenManager.setOnboardingCompleted(true)
            val isLogin = tokenManager.isLoggedIn()
            val nextRoute = if (isLogin) Screen.Main.route else Screen.Auth.route
            navController.navigate(nextRoute) {
                popUpTo(Screen.OnBoarding.route) { inclusive = true }
                popUpTo(Screen.PermissionRequest.route) { inclusive = true }
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            isNotificationGranted = result[Manifest.permission.POST_NOTIFICATIONS] ?: isNotificationGranted
        }
        isCameraGranted = result[Manifest.permission.CAMERA] ?: isCameraGranted
        isLocationGranted = (result[Manifest.permission.ACCESS_FINE_LOCATION] == true) ||
                (result[Manifest.permission.ACCESS_COARSE_LOCATION] == true) || isLocationGranted

        proceedToNextScreen()
    }

    fun requestAllPermissions() {
        val permissionsToRequest = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!isNotificationGranted) {
                permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        if (!isCameraGranted) {
            permissionsToRequest.add(Manifest.permission.CAMERA)
        }
        if (!isLocationGranted) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (permissionsToRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            proceedToNextScreen()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // ─── Header Icon & Title ──────────────────────────────────────────────
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFFFFEDE0), Color(0xFFFFD8BF))
                    )
                )
                .border(2.dp, DarkOrange.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Shield,
                contentDescription = null,
                tint = DarkOrange,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Izin Akses Aplikasi",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MidnightBlue,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Untuk memberikan pengalaman servis terbaik dan notifikasi panggilan antrean secara real-time, kami memerlukan beberapa izin akses berikut:",
            fontSize = 13.sp,
            color = DimGray,
            lineHeight = 19.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ─── Permission Cards ─────────────────────────────────────────────────
        PermissionCardItem(
            icon = Icons.Default.Notifications,
            iconBg = Color(0xFFE0F2FE),
            iconTint = Color(0xFF0284C7),
            title = "Notifikasi & Getaran Antrean",
            description = "Memberitahu secara instan saat nomor antrean Anda dipanggil oleh mekanik di bengkel dan pengingat servis.",
            isGranted = isNotificationGranted
        )

        Spacer(modifier = Modifier.height(14.dp))

        PermissionCardItem(
            icon = Icons.Default.CameraAlt,
            iconBg = Color(0xFFFEF3C7),
            iconTint = Color(0xFFD97706),
            title = "Kamera (Scan STNK)",
            description = "Memindai otomatis nomor rangka dan nomor mesin motor dari STNK Anda menggunakan OCR.",
            isGranted = isCameraGranted
        )

        Spacer(modifier = Modifier.height(14.dp))

        PermissionCardItem(
            icon = Icons.Default.LocationOn,
            iconBg = Color(0xFFDCFCE7),
            iconTint = Color(0xFF16A34A),
            title = "Lokasi Cabang Terdekat",
            description = "Mendeteksi cabang bengkel resmi Yamaha Lautan Teduh terdekat dari lokasi Anda dan estimasi jarak.",
            isGranted = isLocationGranted
        )

        Spacer(modifier = Modifier.height(32.dp))

        // ─── Action Buttons ───────────────────────────────────────────────────
        Button(
            onClick = { requestAllPermissions() },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .shadow(4.dp, RoundedCornerShape(14.dp)),
            colors = ButtonDefaults.buttonColors(containerColor = MidnightBlue),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "Izinkan & Lanjutkan",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = { proceedToNextScreen() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = DimGray)
        ) {
            Text(
                text = "Nanti Saja",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = DimGray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun PermissionCardItem(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    description: String,
    isGranted: Boolean
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MidnightBlue,
                        modifier = Modifier.weight(1f)
                    )

                    if (isGranted) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Sudah Diizinkan",
                            tint = Color(0xFF16A34A),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = DarkSlate.copy(alpha = 0.75f),
                    lineHeight = 17.sp
                )
            }
        }
    }
}
