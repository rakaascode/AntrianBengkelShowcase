package dev.inteiintel.teduhserviceapp.presentation.main.home.reminder

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import dev.inteiintel.teduhserviceapp.presentation.main.profile.edit_profile.PrimBlue
import dev.inteiintel.teduhserviceapp.presentation.main.profile.edit_profile.SnowWhite
import dev.inteiintel.teduhserviceapp.ui.theme.DarkSlate
import dev.inteiintel.teduhserviceapp.ui.theme.DimGray

@Composable
fun ReminderScreen(navController: NavController) {

    val context = LocalContext.current
    val viewModel: ReminderViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    var notificationEnabled by remember { mutableStateOf(true) }
    var selectedReminder by remember { mutableStateOf("15 Menit") }

    var selectedMethod by remember { mutableStateOf("NOTIF") }
    var showWaDialog by remember { mutableStateOf(false) }
    var waNumber by remember { mutableStateOf("") }
    var waSaved by remember { mutableStateOf(false) }

    val backgroundColor = Color(0xFFF6F6FB)

    // 🔥 HANDLE STATE DARI API
    LaunchedEffect(state) {
        when (state) {
            is ReminderState.Success -> {
                waSaved = true
                showWaDialog = false
            }

            is ReminderState.Error -> {
                // TODO: bisa pakai Toast / Snackbar
            }

            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {

        // HEADER
        Box(
            modifier = Modifier
                .background(SnowWhite)
                .fillMaxWidth()
                .padding(10.dp)
        ) {

            Text(
                text = "Pengingat",
                modifier = Modifier.align(Alignment.Center),
                fontWeight = FontWeight.Bold,
                color = DarkSlate,
                fontSize = 18.sp
            )

            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        }

        HorizontalDivider(color = DimGray)

        LazyColumn(modifier = Modifier.padding(16.dp)) {

            item {

                // SWITCH NOTIF
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Icon(Icons.Outlined.NotificationsNone, null)

                        Spacer(Modifier.width(10.dp))

                        Column(Modifier.weight(1f)) {
                            Text("Pengingat Antrian", fontWeight = FontWeight.Bold)
                            Text(
                                "Kami akan mengingatkan Anda",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        Switch(
                            checked = notificationEnabled,
                            onCheckedChange = { notificationEnabled = it }
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                // METODE
                Text("Metode Pengingat", fontWeight = FontWeight.SemiBold)

                Spacer(Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color.White)
                        .padding(horizontal = 16.dp)
                ) {

                    Column {

                        MethodItem(
                            icon = Icons.Outlined.NotificationsNone,
                            title = "Notifikasi",
                            selected = selectedMethod == "NOTIF",
                            status = "Aktif",
                            onClick = {
                                selectedMethod = "NOTIF"
                            }
                        )

                        HorizontalDivider()

                        MethodItem(
                            icon = Icons.Outlined.Whatsapp,
                            title = "WhatsApp",
                            selected = selectedMethod == "WA",
                            status = if (waSaved) "Aktif" else "Belum diatur",
                            onClick = {

                                selectedMethod = "WA"

                                if (!waSaved) {
                                    showWaDialog = true
                                } else {

                                    val formatted =
                                        if (waNumber.startsWith("0"))
                                            "62${waNumber.drop(1)}"
                                        else waNumber

                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://wa.me/$formatted")
                                    )
                                    context.startActivity(intent)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    // 🔥 DIALOG INPUT WA
    if (showWaDialog) {
        AlertDialog(
            onDismissRequest = { showWaDialog = false },
            title = {
                Text(
                    text = "Nomor WhatsApp",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {

                    Text(
                        text = "Masukkan nomor WhatsApp untuk menerima reminder",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = waNumber,
                        onValueChange = { waNumber = it },
                        placeholder = { Text("08xxxxxxxxxx") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    if (state is ReminderState.Loading) {

                        Spacer(Modifier.height(16.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp
                            )

                            Spacer(Modifier.width(10.dp))

                            Text(
                                text = "Mengirim ke WhatsApp...",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            },
            confirmButton = {

                Button(
                    enabled = waNumber.isNotEmpty() && state !is ReminderState.Loading,
                    onClick = {
                        viewModel.sendWhatsapp(waNumber)
                    }
                ) {
                    Text(
                        if (state is ReminderState.Loading)
                            "Loading..."
                        else
                            "Simpan"
                    )
                }
            },
            dismissButton = {

                OutlinedButton(
                    enabled = state !is ReminderState.Loading,
                    onClick = {
                        showWaDialog = false
                    }
                ) {
                    Text("Batal")
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
fun MethodItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    selected: Boolean,
    status: String,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0xFFF3F4FF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null)
        }

        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {

            Text(title, fontWeight = FontWeight.Medium)

            Text(
                status,
                fontSize = 12.sp,
                color = if (selected) PrimBlue else Color.Gray
            )
        }

        if (selected) {
            Icon(Icons.Outlined.ChevronRight, null)
        }
    }
}