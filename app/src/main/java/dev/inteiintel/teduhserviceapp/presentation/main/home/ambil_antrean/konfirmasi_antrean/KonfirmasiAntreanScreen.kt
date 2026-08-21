package dev.inteiintel.teduhserviceapp.presentation.main.home.ambil_antrean.konfirmasi_antrean

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Motorcycle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import dev.inteiintel.teduhserviceapp.data.model.CreateAntrianRequest
import dev.inteiintel.teduhserviceapp.data.model.ui.KonfirmasiAntreanUiModel
import dev.inteiintel.teduhserviceapp.ui.theme.DarkSlate
import dev.inteiintel.teduhserviceapp.ui.theme.DimGray
import dev.inteiintel.teduhserviceapp.ui.theme.PrimBlue
import dev.inteiintel.teduhserviceapp.ui.theme.SnowWhite

@Composable
fun KonfirmasiAntreanScreen(
    data: KonfirmasiAntreanUiModel,
    request: CreateAntrianRequest,
    isLoading: Boolean = false,
    onBackClick: () -> Unit = {},
    onAmbilAntreanClick: (CreateAntrianRequest) -> Unit = {},
    onBatalClick: () -> Unit = {}
) {

    val backgroundColor = Color(0xFFF8F8FC)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {

        // =========================
        // MAIN CONTENT
        // =========================
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {

            // HEADER
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
                        text = "Konfirmasi Antrean",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkSlate
                    )
                }

                IconButton(
                    onClick = {
                        if (!isLoading) onBackClick()
                    }
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {

                Spacer(modifier = Modifier.height(18.dp))

                CardKonfirmasiCabang(
                    namaCabang = data.namaCabang,
                    alamatCabang = data.alamatCabang,
                )

                Spacer(modifier = Modifier.height(16.dp))

                CardKonfirmasiKendaraan(
                    namaPemilik = data.namaPemilik,
                    nomorPolisi = data.nomorPolisi,
                    merkType = data.merkType,
                )

                Spacer(modifier = Modifier.height(24.dp))

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
                            .background(Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = null,
                            tint = PrimBlue
                        )
                    }

                    Spacer(modifier = Modifier.size(14.dp))

                    Text(
                        text = "Pastikan semua data sesuai. Setelah antrean diambil, data tidak dapat diubah kembali.",
                        color = SnowWhite,
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // BUTTON AMBIL
                Button(
                    onClick = { onAmbilAntreanClick(request) },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimBlue,
                        disabledContainerColor = PrimBlue.copy(alpha = 0.7f)
                    )
                ) {

                    Text(
                        text = if (isLoading) "Memproses..." else "Ambil Antrean",
                        color = SnowWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // BUTTON BATAL
                OutlinedButton(
                    onClick = { if (!isLoading) onBatalClick() },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(18.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        PrimBlue
                    )
                ) {
                    Text(
                        text = "Batal",
                        color = PrimBlue,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // =========================
        // FULL SCREEN LOADING OVERLAY
        // =========================
        if (isLoading) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    CircularProgressIndicator(
                        color = SnowWhite,
                        strokeWidth = 3.dp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Memproses antrean...",
                        color = SnowWhite,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CardKonfirmasiCabang(
    namaCabang: String,
    alamatCabang: String,
) {

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = SnowWhite,
        shape = RoundedCornerShape(20.dp),
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
            verticalAlignment = Alignment.Top
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
                    imageVector = Icons.Outlined.LocationOn,
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
                    text = "Kantor Cabang",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF6B7280)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = namaCabang,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimBlue
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = alamatCabang,
                    fontSize = 14.sp,
                    lineHeight = 21.sp,
                    color = DarkSlate
                )
            }
        }
    }
}

@Composable
fun CardKonfirmasiKendaraan(
    namaPemilik: String,
    nomorPolisi: String,
    merkType: String,
) {

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = SnowWhite,
        shape = RoundedCornerShape(20.dp),
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
            verticalAlignment = Alignment.Top
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
                    text = "Data Kendaraan",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF6B7280)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = namaPemilik,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimBlue
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "No. Polisi",
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = nomorPolisi,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkSlate
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Merk / Type",
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280)
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = merkType,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DarkSlate
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewKonfirmasiAntreanScreen() {

    val dummyData = KonfirmasiAntreanUiModel(
        namaCabang = "Teduh Service Rajabasa",
        alamatCabang = "Jl. Z.A Pagar Alam No.12 Bandar Lampung",
        namaPemilik = "Raka Agi Saputra",
        nomorPolisi = "BE 2879 FL",
        merkType = "Yamaha Mio M3"
    )

    val dummyRequest = CreateAntrianRequest(
        cabang_id = 1,
        nama_pemilik = "Raka",
        no_hp = "08123",
        merk_motor = "Yamaha",
        tipe_motor = "Mio M3",
        no_rangka = "123",
        no_mesin = "456",
        tahun_pembuatan = 2022,
        tanggal_kedatangan = "2026-05-15",
        estimasi_jam = "10:00"
    )

    KonfirmasiAntreanScreen(
        data = dummyData,
        request = dummyRequest,
        isLoading = true
    )
}