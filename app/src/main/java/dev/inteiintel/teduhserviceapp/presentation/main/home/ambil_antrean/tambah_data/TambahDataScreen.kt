package dev.inteiintel.teduhserviceapp.presentation.main.home.ambil_antrean.tambah_data

import android.annotation.SuppressLint
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.inteiintel.teduhserviceapp.data.model.AntrianFormData
import dev.inteiintel.teduhserviceapp.data.model.CreateAntrianRequest
import dev.inteiintel.teduhserviceapp.ui.theme.DimGray
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun TambahDataScreen(
    onClickScanOCR: ()-> Unit = {},
    onCancelClick: () -> Unit = {},
    onNextScreenClick: (CreateAntrianRequest)-> Unit = {},
    onSaveToLocal: (CreateAntrianRequest) -> Unit = {}
) {

    var namaLengkap by remember { mutableStateOf("") }
    var tipeMotor by remember { mutableStateOf("") }
    var noPolisi by remember { mutableStateOf("") }
    var tahunPembuatan by remember { mutableStateOf("") }
    var noRangka by remember { mutableStateOf("") }
    var noMesin by remember { mutableStateOf("") }

    var saveToLocal by remember { mutableStateOf(false) }


    var tanggalKedatangan by remember { mutableStateOf("") }
    var jamKedatangan by remember { mutableStateOf("") }

    val isFormValid =
        namaLengkap.isNotBlank() &&
                tipeMotor.isNotBlank() &&
                noPolisi.isNotBlank() &&
                noRangka.isNotBlank() &&
                noMesin.isNotBlank() &&
                tahunPembuatan.isNotBlank() &&
                tanggalKedatangan.isNotBlank() &&
                jamKedatangan.isNotBlank()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F5F7))
    ) {


        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = 150.dp
            )
        ) {


            item { Spacer(modifier = Modifier.height(16.dp)) }

            item { InfoCard() }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item { SectionScan(
                onClick = {
                    onClickScanOCR()
                }
            ) }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item { DividerManual() }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item { FormInput(stateValue = namaLengkap, onValueChange = {namaLengkap = it},"Nama Lengkap Pemilik", "Sesuai STNK") }


            item {
                Row {
                    FormInput(stateValue = tipeMotor, onValueChange = {tipeMotor = it},"Tipe Motor", "Yamaha Fazzio",Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    FormInput(stateValue = noPolisi, onValueChange = {noPolisi = it},"No Polisi", "BE...", Modifier.weight(1f))
                }
            }

            item {
                Row {
                    FormInput(stateValue = noRangka, onValueChange = {noRangka = it},"No. Rangka", "MH1...", Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))
                    FormInput(stateValue = noMesin, onValueChange = {noMesin = it},"No. Mesin", "JM1...", Modifier.weight(1f))
                }
            }

            item { FormInput(stateValue = tahunPembuatan, onValueChange = {tahunPembuatan = it},"Tahun Pembuatan", "Pilih Tahun") }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                Text("Estimasi Kedatangan", fontWeight = FontWeight.Bold)
            }

            item {
                DateTimeInputSection(
                    tanggalKedatangan = tanggalKedatangan,
                    onTanggalChange = { tanggalKedatangan = it },
                    jamKedatangan = jamKedatangan,
                    onJamChange = { jamKedatangan = it }
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item { SaveSwitchCard(checked = saveToLocal, onCheckedChange = {saveToLocal = it }) }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {

            Button(
                onClick = {
                    val dataReq = CreateAntrianRequest(
                        cabang_id = 0,
                        nama_pemilik = namaLengkap,
                        no_hp = noPolisi,
                        merk_motor = "Yamaha",
                        tipe_motor = tipeMotor,
                        no_rangka = noRangka,
                        no_mesin = noMesin,
                        tahun_pembuatan = tahunPembuatan.toIntOrNull() ?: 0,
                        tanggal_kedatangan = convertToIsoDate(tanggalKedatangan),
                        estimasi_jam = jamKedatangan,
                        catatan = "",
                        reminder_aktif = false,
                        no_wa_reminder = ""
                    )

                    if(saveToLocal){
                        onSaveToLocal(dataReq)
                    }

                    onNextScreenClick(dataReq)
                },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E2A5A),
                    disabledContainerColor = Color.Gray
                )
            ) {
                Text("Pilih Cabang", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    onCancelClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Batal")
            }
        }
    }
}

fun convertToIsoDate(input: String): String {
    return try {

        val parser = SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.getDefault()
        )

        val date = parser.parse(input)

        val formatter = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            Locale.US
        )

        formatter.format(date!!)

    } catch (e: Exception) {
        ""
    }
}

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimeInputSection(
    tanggalKedatangan: String,
    onTanggalChange: (String) -> Unit,
    jamKedatangan: String,
    onJamChange: (String) -> Unit
) {

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    Column {

        OutlinedTextField(
            value = tanggalKedatangan,
            onValueChange = {},
            readOnly = true,
            label = { Text("Tanggal Kedatangan") },
            placeholder = { Text("Pilih tanggal") },
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // =========================
        // ⏰ JAM INPUT
        // =========================
        OutlinedTextField(
            value = jamKedatangan,
            onValueChange = {},
            readOnly = true,
            label = { Text("Jam Kedatangan") },
            placeholder = { Text("Pilih jam") },
            trailingIcon = {
                IconButton(onClick = { showTimePicker = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }

    if (showDatePicker) {

        val dateState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {

                    val millis = dateState.selectedDateMillis

                    onTanggalChange(
                        millis?.let {
                            SimpleDateFormat(
                                "dd/MM/yyyy",
                                Locale.getDefault()
                            ).format(Date(it))
                        } ?: ""
                    )

                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Batal")
                }
            }
        ) {
            DatePicker(state = dateState)
        }
    }

    if (showTimePicker) {

        val timeState = rememberTimePickerState()

        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {

                    onJamChange(
                        String.format(
                            "%02d:%02d",
                            timeState.hour,
                            timeState.minute
                        )
                    )

                    showTimePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Batal")
                }
            },
            text = {
                TimePicker(state = timeState)
            }
        )
    }
}

@Composable
fun InfoCard() {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE9ECF5))
    ) {
        Row(modifier = Modifier.padding(12.dp)) {

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Color(0xFF1E2A5A), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("i", color = Color.White, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    "Siapkan STNK Anda",
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Pendaftaran antrian service membutuhkan data dari STNK. Pastikan data yang dimasukkan sesuai.",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun SectionScan(
    onClick: () -> Unit
) {

    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Pindai STNK", fontWeight = FontWeight.Bold)

            Text(
                "Lebih Cepat",
                fontSize = 12.sp,
                color = Color(0xFF5C6BC0),
                modifier = Modifier
                    .background(
                        Color(0xFFE8EAF6),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .drawBehind {
                    val stroke = Stroke(
                        width = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                    )
                    drawRoundRect(
                        color = Color.LightGray,
                        style = stroke,
                        cornerRadius = CornerRadius(12.dp.toPx())
                    )
                }.clickable(
                    enabled = true,
                    onClick = {
                        onClick()
                    }
                ),
            contentAlignment = Alignment.Center
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color(0xFFEDEDED), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = null,
                        tint = Color(0xFF1E2A5A)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("Scan Foto STNK", fontWeight = FontWeight.Medium)

                Text(
                    "Kami akan membaca data otomatis \ndari foto STNK Anda.",
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun DividerManual() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp, color = DimGray
        )
        Text(
            "  ATAU ISI MANUAL  ",
            fontSize = 12.sp,
            color = Color.Gray
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = DimGray
        )
    }
}


@Composable
fun FormInput(
    stateValue: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    trailingIcon: (@Composable (() -> Unit))? = null
) {
    Column(modifier = modifier.padding(vertical = 6.dp)) {

        Text(label, fontSize = 12.sp)

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = stateValue,
            onValueChange = onValueChange,
            placeholder = {
                Text(placeholder, fontSize = 12.sp)
            },
            trailingIcon = trailingIcon,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedBorderColor = Color(0xFF1E2A5A)
            )
        )
    }
}

@Composable
fun SaveSwitchCard(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text("Simpan Data Kendaraan")

                Text(
                    "Simpan untuk pendaftaran service berikutnya.",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewAmbilAntreanScreen(){
    TambahDataScreen()
}



//val dataReq = CreateAntrianRequest(
//    cabang_id = 0,
//    nama_pemilik = namaLengkap,
//    no_hp = noPolisi,
//    merk_motor = "Yamaha",
//    tipe_motor = tipeMotor,
//    no_rangka = noRangka,
//    no_mesin = noMesin,
//    tahun_pembuatan = tahunPembuatan.toIntOrNull() ?: 0,
//    tanggal_kedatangan = convertToIsoDate(tanggalKedatangan),
//    estimasi_jam = jamKedatangan,
//    catatan = "",
//    reminder_aktif = false,
//    no_wa_reminder = ""
//)