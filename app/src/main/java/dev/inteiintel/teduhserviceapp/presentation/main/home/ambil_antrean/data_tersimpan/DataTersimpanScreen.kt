package dev.inteiintel.teduhserviceapp.presentation.main.home.ambil_antrean.data_tersimpan

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Motorcycle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.inteiintel.teduhserviceapp.R
import dev.inteiintel.teduhserviceapp.data.model.ui.KendaraanTersimpanUiModel
import dev.inteiintel.teduhserviceapp.ui.theme.DarkSlate
import dev.inteiintel.teduhserviceapp.ui.theme.DimGray
import dev.inteiintel.teduhserviceapp.ui.theme.PrimBlue
import dev.inteiintel.teduhserviceapp.ui.theme.SnowWhite
import dev.inteiintel.teduhserviceapp.data.local.room.SavedAntrianEntity


@Composable
fun PilihDataKendaraanScreen(
    kendaraanList: List<SavedAntrianEntity>,
    onBackClick: () -> Unit = {},
    onPilihKendaraan: (SavedAntrianEntity) -> Unit = {}
) {

    val backgroundColor = Color(0xFFF8F8FC)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {

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
                    text = "Pilih Data Tersimpan",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkSlate
                )
            }

            IconButton(
                onClick = onBackClick
            ) {

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = DarkSlate
                )
            }
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = DimGray
        )



        Spacer(modifier = Modifier.height(12.dp))

        if (kendaraanList.isEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(R.drawable.img_not_found),
                    contentDescription = "not found",
                    contentScale = ContentScale.FillWidth
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Saat ini kamu belum memiliki \n data tersimpan",
                    fontSize = 12.sp,
                    color = Color(0xFF7B7E8F),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

        } else {

            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {

                items(kendaraanList) { kendaraan ->

                    Spacer(modifier = Modifier.height(12.dp))

                    CardDataKendaraanTersimpan(
                        namaPemilik = kendaraan.nama_pemilik,
                        nomorPolisi = kendaraan.no_polisi,
                        merkType = kendaraan.merk_motor,
                        onClick = {
                            onPilihKendaraan(kendaraan)
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun CardDataKendaraanTersimpan(
    namaPemilik: String,
    nomorPolisi: String,
    merkType: String,
    onClick: () -> Unit
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                onClick()
            },
        color = Color.White,
        shadowElevation = 3.dp
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color(0xFFE5E7EB),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = PrimBlue,
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Outlined.Motorcycle,
                    contentDescription = null,
                    tint = SnowWhite,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.size(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = namaPemilik,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimBlue
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = nomorPolisi,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkSlate
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = merkType,
                    fontSize = 13.sp,
                    color = Color(0xFF6B7280)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = PrimBlue,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPilihDataKendaraanScreen() {

    val dummyData = listOf(
        KendaraanTersimpanUiModel(
            id = 1,
            namaPemilik = "Raka Agi Saputra",
            nomorPolisi = "BE 2879 FL",
            merkType = "Yamaha Mio M3"
        ),
        KendaraanTersimpanUiModel(
            id = 2,
            namaPemilik = "Andi Setiawan",
            nomorPolisi = "B 1234 ABC",
            merkType = "Yamaha NMAX 155"
        ),
        KendaraanTersimpanUiModel(
            id = 3,
            namaPemilik = "Rina Zulkarnain",
            nomorPolisi = "D 4567 KLM",
            merkType = "Yamaha Aerox 155"
        )
    )

    val dummy = listOf<SavedAntrianEntity>()

    PilihDataKendaraanScreen(
        kendaraanList = dummy
    )
}