package dev.inteiintel.teduhserviceapp.presentation.main.home.detail_cabang

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.inteiintel.teduhserviceapp.R
import dev.inteiintel.teduhserviceapp.data.model.Branch
import dev.inteiintel.teduhserviceapp.data.model.RingkasanCabangItem
import dev.inteiintel.teduhserviceapp.ui.theme.DarkSlate
import dev.inteiintel.teduhserviceapp.ui.theme.DimGray
import dev.inteiintel.teduhserviceapp.ui.theme.GhostWhite
import dev.inteiintel.teduhserviceapp.ui.theme.PrimBlue
import dev.inteiintel.teduhserviceapp.ui.theme.SnowWhite
import java.util.Locale

@Composable
fun DetailCabangScreen(
    detailCabangViewModel: DetailCabangViewModel = hiltViewModel(),
    navController: NavController,
    branchId: Int
) {

    val dataBranch =
        detailCabangViewModel.dataBranch.collectAsState().value

    val ringkasanCabang =
        detailCabangViewModel.ringkasanCabang.collectAsState().value

    LaunchedEffect(Unit) {

        detailCabangViewModel.loadDataBranch(branchId)

        detailCabangViewModel.loadRingkasanCabang(branchId)
    }

    Column(
        modifier = Modifier.fillMaxSize()
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
                    "Detail Cabang",
                    fontWeight = FontWeight.Bold,
                    color = DarkSlate
                )
            }

            IconButton(
                onClick = {
                    navController.popBackStack()
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

        ContentDetailCabang(
            modifier = Modifier.weight(1f),
            dataBranch = dataBranch,
            ringkasanCabang = ringkasanCabang
        )

        HorizontalDivider(
            thickness = 1.dp,
            color = DimGray
        )
    }
}

@Composable
fun ContentDetailCabang(
    modifier: Modifier,
    dataBranch: Branch?,
    ringkasanCabang: RingkasanCabangItem?
) {

    val context = LocalContext.current

    val showQueueCard = ringkasanCabang != null &&
            ringkasanCabang.nomorDipanggil != null &&
            ringkasanCabang.nomorDipanggil != 0

    Column(modifier) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = SnowWhite)
        ) {

            item {

                Image(
                    painter = painterResource(R.drawable.img_kantor),
                    contentDescription = null,
                    modifier = Modifier.aspectRatio(2f),
                    contentScale = ContentScale.FillBounds
                )

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = 10.dp)
                ) {

                    Text(
                        text = dataBranch?.nama
                            ?.replaceFirstChar {
                                if (it.isLowerCase()) {
                                    it.titlecase(Locale.getDefault())
                                } else {
                                    it.toString()
                                }
                            } ?: "",
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(12.dp))

                    Row(
                        Modifier.padding(horizontal = 20.dp)
                    ) {

                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null
                        )

                        Spacer(Modifier.width(12.dp))

                        Text(
                            text = dataBranch?.alamat ?: "",
                            fontSize = 14.sp
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    Row(
                        Modifier
                            .padding(horizontal = 20.dp)
                            .clickable {

                                val latitude =
                                    dataBranch?.latitude ?: ""

                                val longitude =
                                    dataBranch?.longitude ?: ""

                                val gmmIntentUri =
                                    Uri.parse(
                                        "google.navigation:q=$latitude,$longitude"
                                    )

                                val mapIntent = Intent(
                                    Intent.ACTION_VIEW,
                                    gmmIntentUri
                                ).apply {
                                    setPackage("com.google.android.apps.maps")
                                }

                                context.startActivity(mapIntent)
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = null
                        )

                        Spacer(Modifier.width(12.dp))

                        Text(
                            "Get Direction",
                            fontSize = 14.sp
                        )
                    }
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = DimGray
                )

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = 10.dp)
                ) {

                    Text(
                        "Jam Operasional",
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(12.dp))

                    Row(
                        Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            "Senin - Jumat",
                            fontSize = 14.sp
                        )

                        Text(
                            "09:00 Pagi - 5:00 Sore",
                            fontSize = 14.sp
                        )
                    }

                    Row(
                        Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            "Sabtu",
                            fontSize = 14.sp
                        )

                        Text(
                            "09:00 Pagi - 5:00 Sore",
                            fontSize = 14.sp
                        )
                    }

                    Row(
                        Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            "Minggu",
                            fontSize = 14.sp
                        )

                        Text(
                            "Tutup",
                            fontSize = 14.sp
                        )
                    }
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = DimGray
                )

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 10.dp)
                ) {

                    Spacer(Modifier.height(12.dp))

                    Text(
                        "Informasi Kontak",
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(12.dp))

                    Row(
                        Modifier.padding(horizontal = 20.dp)
                    ) {

                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = null
                        )

                        Spacer(Modifier.width(12.dp))

                        Column {

                            Text(
                                "Phone",
                                fontSize = 14.sp
                            )

                            Spacer(Modifier.height(2.dp))

                            Text(
                                text = dataBranch?.no_telp ?: "",
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Row(
                        Modifier.padding(horizontal = 20.dp)
                    ) {

                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null
                        )

                        Spacer(Modifier.width(12.dp))

                        Column {

                            Text(
                                "Email",
                                fontSize = 14.sp
                            )

                            Spacer(Modifier.height(2.dp))

                            Text(
                                "kantorcabang@gmail.com",
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = DimGray
                )

                AnimatedVisibility(
                    visible = showQueueCard,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp, horizontal = 10.dp)
                    ) {

                        Spacer(Modifier.height(12.dp))

                        Text(
                            "Jumlah Antrian Hari ini",
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(12.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .shadow(
                                        10.dp,
                                        RoundedCornerShape(12.dp)
                                    )
                            ) {

                                Column(
                                    Modifier
                                        .background(GhostWhite)
                                        .fillMaxWidth()
                                        .padding(12.dp)
                                ) {

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Icon(
                                            imageVector = Icons.Default.AccountCircle,
                                            contentDescription = null,
                                            modifier = Modifier.size(34.dp),
                                        )

                                        Spacer(Modifier.width(4.dp))

                                        Column {

                                            Text(
                                                text = "A${ringkasanCabang?.nomorDipanggil}",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            )

                                            Spacer(Modifier.height(2.dp))

                                            Text(
                                                text = "${ringkasanCabang?.sisaAntrian} sedang dalam antrian",
                                                fontSize = 14.sp,
                                                color = DimGray
                                            )
                                        }
                                    }

                                    Spacer(Modifier.height(12.dp))

                                    Text(
                                        text = "Estimasi Waktu Tunggu: ${ringkasanCabang?.estimasiJam}",
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ButtonAmbilAntrian(
    action: () -> Unit
) {

    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 20.dp)
    ) {

        Box(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable {
                    action()
                }
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PrimBlue)
                    .padding(vertical = 10.dp, horizontal = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                Icon(
                    Icons.Default.AddCircle,
                    contentDescription = null,
                    tint = SnowWhite
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    "Ambil Antrian",
                    color = SnowWhite
                )
            }
        }
    }
}