package dev.inteiintel.teduhserviceapp.presentation.main.home.ambil_antrean.pilih_estimasi

import android.annotation.SuppressLint
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.inteiintel.teduhserviceapp.data.model.CreateAntrianRequest
import dev.inteiintel.teduhserviceapp.ui.theme.DarkSlate
import dev.inteiintel.teduhserviceapp.ui.theme.DimGray
import dev.inteiintel.teduhserviceapp.ui.theme.PrimBlue
import dev.inteiintel.teduhserviceapp.ui.theme.SnowWhite
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PilihEstimasiScreen(
    request: CreateAntrianRequest,
    onBackClick: () -> Unit = {},
    onLanjutClick: (CreateAntrianRequest) -> Unit = {}
) {
    var tanggalKedatangan by remember { mutableStateOf("") }
    var jamKedatangan by remember { mutableStateOf("") }
    var reminderAktif by remember { mutableStateOf(request.reminder_aktif) }
    var noWaReminder by remember { mutableStateOf(request.no_wa_reminder ?: "") }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val isFormValid = tanggalKedatangan.isNotBlank() && jamKedatangan.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8FC))
    ) {

        // ─── Top App Bar ──────────────────────────────────────────────────────
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
                    text = "Estimasi Kedatangan",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkSlate
                )
            }

            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    modifier = Modifier.size(24.dp),
                    tint = DarkSlate
                )
            }
        }

        HorizontalDivider(thickness = 1.dp, color = DimGray)

        // ─── Content ──────────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {

            // Info ringkasan kendaraan
            RingkasanKendaraanCard(request = request)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Pilih Waktu Kedatangan",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = DarkSlate
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Tentukan tanggal dan jam estimasi kedatangan Anda ke bengkel.",
                fontSize = 12.sp,
                color = Color(0xFF6B7280)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ─── Tanggal Picker ───────────────────────────────────────────────
            Text("Tanggal Kedatangan", fontSize = 12.sp, color = DarkSlate)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = tanggalKedatangan,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Pilih tanggal", fontSize = 12.sp, color = Color(0xFF9CA3AF)) },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            tint = PrimBlue
                        )
                    }
                },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.CalendarToday,
                        contentDescription = null,
                        tint = PrimBlue,
                        modifier = Modifier.size(20.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = SnowWhite,
                    focusedContainerColor = SnowWhite,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = PrimBlue
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ─── Jam Picker ───────────────────────────────────────────────────
            Text("Jam Kedatangan", fontSize = 12.sp, color = DarkSlate)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = jamKedatangan,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Pilih jam", fontSize = 12.sp, color = Color(0xFF9CA3AF)) },
                trailingIcon = {
                    IconButton(onClick = { showTimePicker = true }) {
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = PrimBlue
                        )
                    }
                },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.AccessTime,
                        contentDescription = null,
                        tint = PrimBlue,
                        modifier = Modifier.size(20.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = SnowWhite,
                    focusedContainerColor = SnowWhite,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = PrimBlue
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ─── Pengingat WhatsApp ───────────────────────────────────────────
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SnowWhite),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Pengingat WhatsApp (H-30)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = DarkSlate)
                            Text(
                                "Kirim notifikasi otomatis H-30 menit dan saat nomor dipanggil ke WhatsApp Anda.",
                                fontSize = 12.sp,
                                color = Color(0xFF6B7280)
                            )
                        }
                        Switch(
                            checked = reminderAktif,
                            onCheckedChange = { reminderAktif = it }
                        )
                    }

                    if (reminderAktif) {
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = noWaReminder,
                            onValueChange = { noWaReminder = it },
                            label = { Text("Nomor WhatsApp Penerima", fontSize = 12.sp) },
                            placeholder = { Text("Contoh: 081234567890", fontSize = 12.sp) },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimBlue,
                                unfocusedBorderColor = Color(0xFFE0E0E0)
                            )
                        )
                    }
                }
            }
        }

        // ─── Bottom Button ────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SnowWhite)
                .padding(16.dp)
        ) {
            Button(
                onClick = {
                    val finalRequest = request.copy(
                        tanggal_kedatangan = convertToIsoDateEstimasi(tanggalKedatangan),
                        estimasi_jam = jamKedatangan,
                        reminder_aktif = reminderAktif,
                        no_wa_reminder = if (reminderAktif) noWaReminder.trim() else ""
                    )
                    onLanjutClick(finalRequest)
                },
                enabled = isFormValid && (!reminderAktif || noWaReminder.isNotBlank()),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimBlue,
                    disabledContainerColor = Color(0xFFBDBDBD)
                )
            ) {
                Text("Pilih Cabang", color = SnowWhite, fontWeight = FontWeight.SemiBold)
            }
        }
    }

    // ─── Date Picker Dialog ───────────────────────────────────────────────────
    if (showDatePicker) {
        val dateState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = dateState.selectedDateMillis
                    tanggalKedatangan = millis?.let {
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
                    } ?: ""
                    showDatePicker = false
                }) { Text("OK", color = PrimBlue) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Batal", color = Color(0xFF6B7280))
                }
            }
        ) {
            DatePicker(state = dateState)
        }
    }

    // ─── Time Picker Dialog ───────────────────────────────────────────────────
    if (showTimePicker) {
        val timeState = rememberTimePickerState()

        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    jamKedatangan = String.format("%02d:%02d", timeState.hour, timeState.minute)
                    showTimePicker = false
                }) { Text("OK", color = PrimBlue) }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Batal", color = Color(0xFF6B7280))
                }
            },
            text = { TimePicker(state = timeState) }
        )
    }
}

// ─── Ringkasan Kendaraan ──────────────────────────────────────────────────────

@Composable
fun RingkasanKendaraanCard(request: CreateAntrianRequest) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SnowWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(PrimBlue, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.CalendarToday,
                        contentDescription = null,
                        tint = SnowWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.size(12.dp))

                Column {
                    Text(
                        text = request.nama_pemilik,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = PrimBlue
                    )
                    Text(
                        text = "${request.merk_motor} ${request.tipe_motor}",
                        fontSize = 13.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = 1.dp,
                color = Color(0xFFF0F0F0)
            )

            InfoRowEstimasi(label = "No. Polisi", value = request.no_polisi)
            Spacer(modifier = Modifier.height(6.dp))
            InfoRowEstimasi(label = "No. Rangka", value = request.no_rangka)
            Spacer(modifier = Modifier.height(6.dp))
            InfoRowEstimasi(label = "Tahun", value = request.tahun_pembuatan.toString())
        }
    }
}

@Composable
fun InfoRowEstimasi(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 13.sp, color = Color(0xFF6B7280))
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = DarkSlate)
    }
}

// ─── Helper ───────────────────────────────────────────────────────────────────

fun convertToIsoDateEstimasi(input: String): String {
    return try {
        val parser = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = parser.parse(input)
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        formatter.format(date!!)
    } catch (e: Exception) {
        ""
    }
}

// ─── Preview ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
fun PreviewPilihEstimasiScreen() {
    PilihEstimasiScreen(
        request = CreateAntrianRequest(
            cabang_id = 0,
            nama_pemilik = "Raka Agi Saputra",
            no_polisi = "BE 2879 FL",
            merk_motor = "Yamaha",
            tipe_motor = "Mio M3",
            no_rangka = "MH35SG123456",
            no_mesin = "5SG123456",
            tahun_pembuatan = 2021,
            tanggal_kedatangan = "",
            estimasi_jam = ""
        )
    )
}
