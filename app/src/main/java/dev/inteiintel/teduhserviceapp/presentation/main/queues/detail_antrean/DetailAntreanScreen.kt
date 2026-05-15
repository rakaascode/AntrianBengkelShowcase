package dev.inteiintel.teduhserviceapp.presentation.main.queues.detail_antrean


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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.DirectionsBike
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.material.icons.outlined.ViewInAr
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.inteiintel.teduhserviceapp.data.model.AntreanActiveData
import dev.inteiintel.teduhserviceapp.presentation.main.queues.getDateOnly
import dev.inteiintel.teduhserviceapp.ui.theme.DimGray
import dev.inteiintel.teduhserviceapp.ui.theme.PrimBlue
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
    ) {

        // HEADER
        Box(modifier = Modifier.background(PrimBlue).fillMaxWidth().padding(vertical = 10.dp, horizontal = 10.dp)){
            Row (modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.Center){
                Text("Detail Antrean", fontSize = 14.sp,fontWeight = FontWeight.Bold, color = SnowWhite)
            }

            IconButton(onClick = {
                onKembaliClick()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = SnowWhite
                )
            }
        }


        HorizontalDivider( thickness = 1.dp, color = DimGray)


        LazyColumn {
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        // TITLE
                        Text(
                            text = "Data Kendaraan (Sesuai STNK)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = PrimBlue
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        // DATA VEHICLE
                        DetailItem(
                            icon = Icons.Outlined.PersonOutline,
                            label = "Nama Pemilik",
                            value = data.nama_pemilik
                        )

                        DetailItem(
                            icon = Icons.Outlined.ConfirmationNumber,
                            label = "No. Polisi",
                            value = data.no_hp
                        )

                        DetailItem(
                            icon = Icons.Outlined.DirectionsBike,
                            label = "Jenis Kendaraan",
                            value = "Sepeda Motor"
                        )

                        DetailItem(
                            icon = Icons.Outlined.Tag,
                            label = "Merk / Tipe",
                            value = "Yamaha / ${data.tipe_motor}"
                        )

                        DetailItem(
                            icon = Icons.Outlined.CalendarMonth,
                            label = "TH. Pembuatan",
                            value = data.tahun_pembuatan.toString()
                        )

                        DetailItem(
                            icon = Icons.Outlined.ViewInAr,
                            label = "No. Rangka",
                            value = data.no_rangka
                        )

                        DetailItem(
                            icon = Icons.Outlined.Settings,
                            label = "No. Mesin",
                            value = data.no_mesin
                        )



                        Spacer(modifier = Modifier.height(20.dp))

                        // JADWAL
                        Text(
                            text = "Jadwal Antrean",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = PrimBlue
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = Color(0xFFF5F7FF),
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .padding(14.dp)
                        ) {

                            Column {

                                DetailItem(
                                    icon = Icons.Outlined.CalendarMonth,
                                    label = "Tanggal Kedatangan",
                                    value = getDateOnly(data.tanggal_kedatangan.toString())
                                )

                                DetailItem(
                                    icon = Icons.Outlined.Schedule,
                                    label = "Jam Kedatangan",
                                    value = data.estimasi_jam,
                                    isLast = true
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(22.dp))

                    }
                }
            }
        }
    }




}

@Composable
fun DetailItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    isLast: Boolean = false
) {

    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF0A145A),
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.size(12.dp))

            Text(
                text = label,
                modifier = Modifier.weight(1f),
                fontSize = 15.sp,
                color = Color(0xFF374151)
            )

            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }

        if (!isLast) {
            HorizontalDivider(
                color = Color(0xFFE5E7EB),
                thickness = 1.dp
            )
        }
    }
}

//
//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
//@Composable
//fun PreviewDetailAntreanScreen() {
//    DetailAntreanScreen(onBackClick = {},,onKembaliClick = {})
//}