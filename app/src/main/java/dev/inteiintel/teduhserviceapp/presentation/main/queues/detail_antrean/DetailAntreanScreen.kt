package dev.inteiintel.teduhserviceapp.presentation.main.queues.detail_antrean

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.DirectionsBike
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.material.icons.outlined.ViewInAr
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.inteiintel.teduhserviceapp.data.model.AntreanActiveData
import dev.inteiintel.teduhserviceapp.presentation.main.queues.getDateOnly
import dev.inteiintel.teduhserviceapp.ui.theme.DarkSlate
import dev.inteiintel.teduhserviceapp.ui.theme.DimGray
import dev.inteiintel.teduhserviceapp.ui.theme.SnowWhite

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailAntreanScreen(
    data: AntreanActiveData,
    onKembaliClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FC))
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
                    text = "Detail Antrean",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DarkSlate
                )
            }

            IconButton(onClick = onKembaliClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    modifier = Modifier.size(24.dp),
                    tint = DarkSlate
                )
            }
        }

        HorizontalDivider(thickness = 1.dp, color = DimGray)

        // ─── Body Konten ─────────────────────────────────────────────────────
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // ── Ticket Header Card: Nomor & Status ───────────────────────
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 3.dp,
                            shape = RoundedCornerShape(20.dp),
                            ambientColor = Color(0xFF0F1A65).copy(alpha = 0.08f)
                        ),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Badge status pill
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFFE8F5E9))
                                .padding(horizontal = 12.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = if (data.status.equals("menunggu", ignoreCase = true)) "Menunggu Antrean" else data.status.replaceFirstChar { it.uppercase() },
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = "NOMOR ANTREAN",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6374AE),
                            letterSpacing = 1.sp
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = "${data.nomor_antrian}",
                            fontSize = 50.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF0F1A65),
                            letterSpacing = (-0.5).sp
                        )
                    }
                }
            }

            item {
                // ── Card Jadwal Kedatangan ───────────────────────────────────
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 3.dp,
                            shape = RoundedCornerShape(20.dp),
                            ambientColor = Color(0xFF0F1A65).copy(alpha = 0.08f)
                        ),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Jadwal Kedatangan",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF0F1A65)
                        )

                        Spacer(Modifier.height(16.dp))

                        DetailItemRow(
                            icon = Icons.Outlined.CalendarMonth,
                            label = "Tanggal Kedatangan",
                            value = getDateOnly(data.tanggal_kedatangan)
                        )

                        DetailItemRow(
                            icon = Icons.Outlined.AccessTime,
                            label = "Estimasi Jam",
                            value = data.estimasi_jam,
                            isLast = true
                        )
                    }
                }
            }

            item {
                // ── Card Data Kendaraan (STNK) ───────────────────────────────
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 3.dp,
                            shape = RoundedCornerShape(20.dp),
                            ambientColor = Color(0xFF0F1A65).copy(alpha = 0.08f)
                        ),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Data Kendaraan (Sesuai STNK)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF0F1A65)
                        )

                        Spacer(Modifier.height(16.dp))

                        DetailItemRow(
                            icon = Icons.Outlined.PersonOutline,
                            label = "Nama Pemilik",
                            value = data.nama_pemilik
                        )

                        DetailItemRow(
                            icon = Icons.Outlined.Phone,
                            label = "No. Telepon",
                            value = data.no_hp
                        )

                        DetailItemRow(
                            icon = Icons.Outlined.DirectionsBike,
                            label = "Merk / Tipe",
                            value = "${data.merk_motor.ifBlank { "Yamaha" }} / ${data.tipe_motor}"
                        )

                        DetailItemRow(
                            icon = Icons.Outlined.CalendarMonth,
                            label = "Tahun Pembuatan",
                            value = data.tahun_pembuatan.toString()
                        )

                        DetailItemRow(
                            icon = Icons.Outlined.ViewInAr,
                            label = "No. Rangka",
                            value = data.no_rangka
                        )

                        DetailItemRow(
                            icon = Icons.Outlined.Settings,
                            label = "No. Mesin",
                            value = data.no_mesin,
                            isLast = true
                        )
                    }
                }
            }
        }
    }
}

// ─── Baris Detail Reusable ───────────────────────────────────────────────────
@Composable
fun DetailItemRow(
    icon: ImageVector,
    label: String,
    value: String,
    isLast: Boolean = false
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFEFF2FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF0F1A65),
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = label,
                modifier = Modifier.weight(1f),
                fontSize = 13.sp,
                color = Color(0xFF6B7280)
            )

            Text(
                text = value.ifBlank { "-" },
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF111827),
                textAlign = TextAlign.End
            )
        }

        if (!isLast) {
            HorizontalDivider(
                color = Color(0xFFF3F4F6),
                thickness = 1.dp
            )
        }
    }
}

// Backward compatibility helper
@Composable
fun DetailItem(
    icon: ImageVector,
    label: String,
    value: String,
    isLast: Boolean = false
) {
    DetailItemRow(icon = icon, label = label, value = value, isLast = isLast)
}