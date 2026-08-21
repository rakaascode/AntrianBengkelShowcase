package dev.inteiintel.teduhserviceapp.presentation.main.home.reminder

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

private val GradientPrimary = listOf(
    Color(0xFF0F1A65),
    Color(0xFF1B2B8E),
    Color(0xFF263DB5)
)

private val BrandNavy = Color(0xFF0F1A65)
private val BrandBlue = Color(0xFF263DB5)
private val BrandIndigo = Color(0xFF6374AE)
private val CardBackground = Color.White
private val SurfaceBackground = Color(0xFFF6F8FD)
private val BorderColor = Color(0xFFEEF2F9)
private val AccentGreen = Color(0xFF10B981)
private val AccentGreenBg = Color(0xFFECFDF5)
private val AccentAmber = Color(0xFFF59E0B)
private val AccentAmberBg = Color(0xFFFFFBEB)
private val TextDark = Color(0xFF141B4D)
private val TextMuted = Color(0xFF6B7280)

@Composable
fun ReminderScreen(
    navController: NavController,
    viewModel: ReminderViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val savedWa by viewModel.savedWa.collectAsState()

    var notificationEnabled by remember { mutableStateOf(true) }
    var showWaDialog by remember { mutableStateOf(false) }
    var inputWaNumber by remember { mutableStateOf("") }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadSavedWa()
    }

    LaunchedEffect(state) {
        when (state) {
            is ReminderState.Success -> {
                showWaDialog = false
                snackbarMessage = "Nomor WhatsApp pengingat berhasil disimpan!"
                viewModel.resetState()
            }
            is ReminderState.Error -> {
                snackbarMessage = (state as ReminderState.Error).message
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = {
            snackbarMessage?.let { msg ->
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    action = {
                        TextButton(onClick = { snackbarMessage = null }) {
                            Text("OK", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    },
                    containerColor = BrandNavy,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(msg, fontSize = 13.sp)
                }
            }
        },
        containerColor = SurfaceBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // ── HERO HEADER ───────────────────────────────────────────────────
            item {
                HeroReminderHeader(
                    onBack = { navController.popBackStack() }
                )
            }

            // ── MAIN CONTENT (CARDS) ──────────────────────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .offset(y = (-24).dp)
                ) {
                    // 1. MASTER TOGGLE CARD
                    MasterToggleCard(
                        enabled = notificationEnabled,
                        onCheckedChange = { notificationEnabled = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 2. WHATSAPP REMINDER STATUS CARD
                    WhatsAppReminderCard(
                        enabled = notificationEnabled,
                        savedNumber = savedWa,
                        onSetupClick = {
                            inputWaNumber = savedWa ?: ""
                            showWaDialog = true
                        },
                        onTestWaClick = {
                            savedWa?.let { num ->
                                val formatted = if (num.startsWith("0")) "62${num.drop(1)}" else num
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://wa.me/$formatted?text=Halo%20Teduh%20Service%2C%20saya%20sudah%20mengaktifkan%20pengingat%20antrean.")
                                )
                                context.startActivity(intent)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 3. REMINDER TIMING & INFO CARD
                    ReminderTimingCard(enabled = notificationEnabled)

                    Spacer(modifier = Modifier.height(16.dp))

                    // 4. HOW IT WORKS / BENEFIT CARD
                    HowItWorksCard()

                    Spacer(modifier = Modifier.height(28.dp))
                }
            }
        }
    }

    // ── DIALOG INPUT / EDIT WA ───────────────────────────────────────────────
    if (showWaDialog) {
        WhatsAppInputDialog(
            currentNumber = inputWaNumber,
            isLoading = state is ReminderState.Loading,
            onDismiss = { showWaDialog = false },
            onSave = { number ->
                viewModel.sendWhatsapp(number)
            }
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// UI COMPONENTS
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun HeroReminderHeader(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(brush = Brush.verticalGradient(colors = GradientPrimary))
            .padding(top = 20.dp, bottom = 44.dp, start = 20.dp, end = 20.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = "Pengingat Antrean",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Notifikasi & WhatsApp Real-time",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle banner pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.12f))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.NotificationsActive,
                    contentDescription = null,
                    tint = Color(0xFFFFD54F),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Jangan lewatkan giliran servis! Dapatkan kabar saat nomor Anda dipanggil.",
                    fontSize = 12.sp,
                    color = Color.White,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun MasterToggleCard(
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (enabled) Color(0xFFEDE7F6) else Color(0xFFF3F4F6)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (enabled) Icons.Outlined.NotificationsActive else Icons.Outlined.NotificationsOff,
                        contentDescription = null,
                        tint = if (enabled) Color(0xFF673AB7) else TextMuted,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = "Aktifkan Semua Pengingat",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Text(
                        text = if (enabled) "Notifikasi otomatis aktif" else "Pengingat dinonaktifkan",
                        fontSize = 12.sp,
                        color = if (enabled) AccentGreen else TextMuted
                    )
                }
            }

            Switch(
                checked = enabled,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = BrandBlue,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFD1D5DB)
                )
            )
        }
    }
}

@Composable
private fun WhatsAppReminderCard(
    enabled: Boolean,
    savedNumber: String?,
    onSetupClick: () -> Unit,
    onTestWaClick: () -> Unit
) {
    val isConfigured = !savedNumber.isNullOrBlank()

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE8F5E9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Message,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "WhatsApp Gateway",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Text(
                            text = "Pengingat otomatis via pesan WA",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }

                // Status Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isConfigured) AccentGreenBg else AccentAmberBg)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (isConfigured) "Tersambung" else "Belum Diatur",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isConfigured) AccentGreen else AccentAmber
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Number Display Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF9FAFD))
                    .border(1.dp, BorderColor, RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = if (isConfigured) BrandBlue else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Nomor Penerima WA",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                            Text(
                                text = savedNumber ?: "Belum ada nomor tersimpan",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isConfigured) TextDark else TextMuted
                            )
                        }
                    }

                    IconButton(
                        onClick = onSetupClick,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, BorderColor, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Nomor",
                            tint = BrandBlue,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onSetupClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (isConfigured) "Ubah Nomor" else "Hubungkan WhatsApp",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (isConfigured) {
                    OutlinedButton(
                        onClick = onTestWaClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2E7D32)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2E7D32).copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Buka Chat WA",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReminderTimingCard(enabled: Boolean) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                text = "Kapan Anda Akan Diingatkan?",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(14.dp))

            TimingItem(
                icon = Icons.Outlined.AccessTime,
                title = "Saat Nomor Dipanggil (Urutan Terdepan)",
                desc = "Sistem otomatis mengirim WhatsApp seketika giliran Anda tiba di loket bengkel."
            )

            HorizontalDivider(
                color = BorderColor,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            TimingItem(
                icon = Icons.Outlined.CheckCircleOutline,
                title = "Saat Servis Selesai",
                desc = "Notifikasi pemberitahuan saat kendaraan siap diambil di kasir/parkir."
            )
        }
    }
}

@Composable
private fun TimingItem(
    icon: ImageVector,
    title: String,
    desc: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(Color(0xFFF0F4FF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BrandBlue,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = desc,
                fontSize = 11.sp,
                color = TextMuted,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun HowItWorksCard() {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F5FF)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = BrandBlue,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Bebas Biaya Notifikasi",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandNavy
                )
                Text(
                    text = "Layanan pengingat WhatsApp terhubung langsung dengan server resmi Lautan Teduh tanpa biaya tambahan.",
                    fontSize = 11.sp,
                    color = BrandIndigo,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// DIALOG INPUT NOMOR WHATSAPP
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun WhatsAppInputDialog(
    currentNumber: String,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var numberText by remember { mutableStateOf(currentNumber) }
    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE8F5E9)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Message,
                        contentDescription = null,
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Nomor WhatsApp",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = TextDark
                )
            }
        },
        text = {
            Column {
                Text(
                    text = "Masukkan nomor WhatsApp aktif untuk menerima kabar antrean dari bengkel.",
                    fontSize = 12.sp,
                    color = TextMuted,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = numberText,
                    onValueChange = {
                        numberText = it
                        isError = it.isNotBlank() && it.length < 9
                    },
                    placeholder = { Text("Contoh: 081234567890", fontSize = 13.sp, color = Color.LightGray) },
                    singleLine = true,
                    isError = isError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandBlue,
                        unfocusedBorderColor = BorderColor
                    )
                )

                if (isError) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Format nomor belum sesuai (minimal 9 digit)",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                if (isLoading) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = BrandBlue
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Menyimpan kontak ke server...",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                enabled = numberText.isNotBlank() && !isError && !isLoading,
                onClick = { onSave(numberText.trim()) },
                colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(if (isLoading) "Menyimpan..." else "Simpan Nomor", fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            OutlinedButton(
                enabled = !isLoading,
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Batal")
            }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = CardBackground
    )
}