package dev.inteiintel.teduhserviceapp.presentation.main.home.ambil_antrean.berhasil_ambil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Motorcycle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import dev.inteiintel.teduhserviceapp.R
import dev.inteiintel.teduhserviceapp.ui.theme.GhostWhite
import dev.inteiintel.teduhserviceapp.ui.theme.PrimBlue
import dev.inteiintel.teduhserviceapp.ui.theme.SnowWhite

@Composable
fun BerhasilAmbilAntreanScreen(
    queueNumber: String,
    estDate: String,
    vehicle: String,
    branch: String,
    onBackHome: () -> Unit
) {

    SuccessQueueScreen(
        onBackHome = onBackHome,
        queueNumber = queueNumber,
        estDate = estDate,
        vehicle = vehicle,
        branch = branch,
    )
}



@Composable
fun SuccessQueueScreen(
    queueNumber: String,
    estDate: String,
    vehicle: String,
    branch: String,
    onBackHome: () -> Unit
) {

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.confetti)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SnowWhite)
            .navigationBarsPadding()
    ) {

        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            // ICON SUCCESS
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(
                        color = Color(0xFF2258C5),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = SnowWhite,
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Antrean Berhasil Diambil!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Berikut detail antrean service Anda",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(26.dp))

            // NOMOR ANTREAN
            Text(
                text = queueNumber,
                fontSize = 52.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PrimBlue
            )

            Spacer(modifier = Modifier.height(26.dp))

            // CARD DETAIL
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = GhostWhite,
                shape = RoundedCornerShape(20.dp)
            ) {

                Column(
                    modifier = Modifier.padding(18.dp)
                ) {

                    InfoRow(
                        title = "Cabang Service",
                        value = branch,
                        icon = Icons.Default.LocationOn
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    HorizontalDivider()

                    Spacer(modifier = Modifier.height(14.dp))

                    InfoRow(
                        title = "Estimasi Kedatangan",
                        value = estDate,
                        icon = Icons.Default.Schedule
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    HorizontalDivider()

                    Spacer(modifier = Modifier.height(14.dp))

                    InfoRow(
                        title = "Kendaraan",
                        value = vehicle,
                        icon = Icons.Default.Motorcycle
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onBackHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimBlue
                ),
                shape = RoundedCornerShape(18.dp)
            ) {

                Text(
                    text = "Kembali ke Beranda",
                    color = SnowWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun InfoRow(
    title: String,
    value: String,
    icon: ImageVector
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
                .background(
                    color = Color(0xFFEDE9FE),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimBlue,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = title,
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSuccessQueueScreen() {

    SuccessQueueScreen(
        queueNumber = "A12",
        estDate = "Rabu, 05 Mei 2026",
        vehicle = "BE 1234 ABH - Yamaha NMAX 155",
        branch = "Teduh Service Rajabasa",
        onBackHome = {}
    )
}
