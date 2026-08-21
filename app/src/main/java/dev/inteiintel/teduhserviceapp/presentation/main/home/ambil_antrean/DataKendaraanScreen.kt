package dev.inteiintel.teduhserviceapp.presentation.main.home.ambil_antrean


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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PersonAdd
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.inteiintel.teduhserviceapp.ui.theme.DarkSlate
import dev.inteiintel.teduhserviceapp.ui.theme.DimGray
import dev.inteiintel.teduhserviceapp.ui.theme.PrimBlue
import dev.inteiintel.teduhserviceapp.ui.theme.SnowWhite

@Composable
fun DataKendaraanScreen(
    onBackClick: () -> Unit = {},
    onSavedVehicleClick: () -> Unit = {},
    onAddVehicleClick: () -> Unit = {}
) {

    val backgroundColor = Color(0xFFF8F8FC)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {


        Box(modifier = Modifier.background(SnowWhite).fillMaxWidth().padding(vertical = 10.dp, horizontal = 10.dp)){
            Row (modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.Center){
                Text("Data Kendaraan", fontSize = 14.sp,fontWeight = FontWeight.Bold, color = DarkSlate)
            }

            IconButton(onClick = {
                onBackClick()
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null,
                    modifier = Modifier.size(24.dp), tint = DarkSlate)
            }
        }


        HorizontalDivider( thickness = 1.dp, color = DimGray)


        Spacer(modifier = Modifier.height(12.dp))
        Column (modifier= Modifier.padding(horizontal = 16.dp)){

            Spacer(modifier = Modifier.height(12.dp))


            // CARD DATA TERSIMPAN
            VehicleOptionCard(
                title = "Gunakan Data Tersimpan",
                description = "Gunakan data kendaraan yang sudah tersimpan sebelumnya.",
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Badge,
                        contentDescription = null,
                        tint = SnowWhite,
                        modifier = Modifier.size(24.dp)
                    )
                },

                onClick = onSavedVehicleClick
            )

            Spacer(modifier = Modifier.height(22.dp))

            // CARD TAMBAH DATA
            VehicleOptionCard(
                title = "Pakai atau Tambah Data",
                description = "Gunakan data kendaraan yang belum tersimpan atau tambah data baru.",
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.PersonAdd,
                        contentDescription = null,
                        tint = SnowWhite,
                        modifier = Modifier.size(24.dp)
                    )
                },
                onClick = onAddVehicleClick
            )

            Spacer(modifier = Modifier.height(30.dp))

            // INFO BOX
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF0D1B54),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(18.dp),
                verticalAlignment = Alignment.Top
            ) {

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            color = Color(0xFFFDFDFD),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = Color(0xFF4338CA)
                    )
                }

                Spacer(modifier = Modifier.size(14.dp))

                Text(
                    text = "Pastikan data kendaraan sesuai dengan STNK yang berlaku.",
                    color = SnowWhite,
                    fontSize = 15.sp,
                    lineHeight = 24.sp
                )
            }

        }



    }
}

@Composable
fun VehicleOptionCard(
    title: String,
    description: String,
    icon: @Composable () -> Unit,
    borderColor: Color = Color.Transparent,
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
                    width = if (borderColor != Color.Transparent) 2.dp else 0.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(28.dp)
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        color = PrimBlue,
                        shape = RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                icon()
            }

            Spacer(modifier = Modifier.size(18.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimBlue
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    fontSize = 14.sp,
                    lineHeight = 19.sp,
                    color = Color(0xFF6B7280)
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

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
fun PreviewAmbilScreen() {
    DataKendaraanScreen(onBackClick = {}, onAddVehicleClick = {}, onSavedVehicleClick = {})
}
