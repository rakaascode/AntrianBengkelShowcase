package dev.inteiintel.teduhserviceapp.presentation.main.queues

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.inteiintel.teduhserviceapp.R
import dev.inteiintel.teduhserviceapp.data.model.AntreanActiveData
import dev.inteiintel.teduhserviceapp.ui.theme.DimGray
import dev.inteiintel.teduhserviceapp.ui.theme.PrimBlue
import dev.inteiintel.teduhserviceapp.utils.navigation.Screen
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QueuesScreen(
    queueViewModel: QueusViewModel = hiltViewModel(),
    navController: NavController
) {

    val list by queueViewModel.antreanMenunggu.collectAsState()
    val loadingCancel by queueViewModel.loadingCancel.collectAsState()

    var selectedCancel by remember {
        mutableStateOf<AntreanActiveData?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Antrean", fontWeight = FontWeight.Bold)
        }

        HorizontalDivider(color = DimGray)

        // EMPTY STATE
        if (list.isEmpty()) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(R.drawable.img_not_found),
                    contentDescription = null
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Saat ini kamu belum memiliki antrean",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }

        } else {

            LazyColumn {

                items(list) { item ->

                    Box(modifier= Modifier.fillMaxWidth().padding(16.dp)){
                        QueueCardModern(
                            data = item,
                            onDetailClick = { selected ->

                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("antrean", selected)

                                navController.navigate(Screen.DetailAntreanActive.route)
                            },
                            onCancelClick = { selected -> selectedCancel = selected }
                        )

                    }

                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }

    selectedCancel?.let { data ->

        AlertDialog(
            onDismissRequest = {
                if (!loadingCancel) selectedCancel = null
            },
            title = { Text("Batalkan Antrean") },
            text = {
                Text("Yakin ingin membatalkan antrean nomor ${data.nomor_antrian}?")
            },
            confirmButton = {

                Button(
                    enabled = !loadingCancel,
                    onClick = {

                        queueViewModel.batalAntrean(
                            id = data.id,
                            onSuccess = { msg ->

                                Toast.makeText(
                                    navController.context,
                                    msg,
                                    Toast.LENGTH_SHORT
                                ).show()

                                selectedCancel = null
                            },
                            onError = { err ->

                                Toast.makeText(
                                    navController.context,
                                    err,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                ) {

                    Text(if (loadingCancel) "Loading..." else "Ya")
                }
            },
            dismissButton = {
                OutlinedButton(
                    enabled = !loadingCancel,
                    onClick = { selectedCancel = null }
                ) {
                    Text("Tidak")
                }
            }
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun QueueCardModern(
    data: AntreanActiveData,
    onDetailClick: (AntreanActiveData) -> Unit,
    onCancelClick: (AntreanActiveData) -> Unit
) {

    val canCancel = data.status == "menunggu"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Column(Modifier.padding(18.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Antrean Kamu",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Text(
                    text = data.status,
                    color = Color(0xFF2E7D32)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "${data.nomor_antrian}",
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier= Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(data.estimasi_jam)
                Text(getDateOnly(data.tanggal_kedatangan))
            }

            Spacer(Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedButton(
                    enabled = canCancel,
                    onClick = {
                        onCancelClick(data)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (canCancel) "Batalkan" else "Tidak bisa")
                }

                Button(
                    onClick = { onDetailClick(data) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Lihat")
                }
            }
        }
    }
}

@Composable
fun QueueStatusBadge(
    status: String
) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(Color(0xFFE8F5E9))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {

        Text(
            text = status,
            color = Color(0xFF2E7D32),
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        )
    }
}

@Composable
fun QueueInfoItem(
    title: String,
    value: String
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = title,
            color = Color.Gray,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getDateOnly(date: String): String {
    val zdt = ZonedDateTime.parse(date)

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    return zdt.toLocalDate().format(formatter)
}

//
//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
//@Composable
//fun PreviewQueuesScreen(){
//    QueuesScreen()
//}