package dev.inteiintel.teduhserviceapp.presentation.main.home.history


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.Motorcycle
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.inteiintel.teduhserviceapp.R
import dev.inteiintel.teduhserviceapp.data.model.Antrian
import dev.inteiintel.teduhserviceapp.presentation.main.queues.getDateOnly

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryScreen(
    historyViewModel: HistoryViewModel = hiltViewModel(),
    navController: NavController
) {

    val antreanData by historyViewModel.dataRiwayatAntrean.collectAsState()

    var selectedTab by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(Unit) {
        historyViewModel.loadUserData()
    }

    val filteredData = if (selectedTab == 0) {
        antreanData.filter {
            it.status.lowercase() != "selesai"
        }
    } else {
        antreanData.filter {
            it.status.lowercase() == "selesai"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {

        Column {

            Spacer(modifier = Modifier.height(12.dp))

            TopBarHistory(navController = navController)

            Spacer(modifier = Modifier.height(16.dp))

            HistoryTab(
                selectedTab = selectedTab,
                onSelected = {
                    selectedTab = it
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                contentPadding = PaddingValues(
                    start = 18.dp,
                    end = 18.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                if (filteredData.isEmpty()) {

                    item {

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
                                text = "Saat ini kamu belum memiliki antrean \n selesai",
                                fontSize = 12.sp,
                                color = Color(0xFF7B7E8F),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                } else {

                    items(filteredData) { item ->
                        HistoryCard(item = item)
                    }
                }
            }
        }
    }
}

@Composable
fun TopBarHistory(navController: NavController) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
    ) {

        IconButton(
            onClick = {
                navController.popBackStack()
            }
        ) {

            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = null,
                tint = Color(0xFF141B4D)
            )
        }

        Text(
            text = "Riwayat Antrean",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF141B4D),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun HistoryTab(
    selectedTab: Int,
    onSelected: (Int) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .height(42.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                1.dp,
                Color(0xFFD4D7E2),
                RoundedCornerShape(8.dp)
            )
    ) {

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(
                    if (selectedTab == 0)
                        Color(0xFF111C67)
                    else
                        Color.Transparent
                )
                .clickable {
                    onSelected(0)
                },
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "Dibatalkan",
                color = if (selectedTab == 0)
                    Color.White
                else
                    Color(0xFF141B4D),
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(
                    if (selectedTab == 1)
                        Color(0xFF111C67)
                    else
                        Color.Transparent
                )
                .clickable {
                    onSelected(1)
                },
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "Selesai",
                color = if (selectedTab == 1)
                    Color.White
                else
                    Color(0xFF141B4D),
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryCard(
    item: Antrian?
) {

    val selesai = item?.status?.trim()?.lowercase() == "selesai"

    ElevatedCard(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(14.dp)
        ) {

            Row(
                verticalAlignment = Alignment.Top
            ) {

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF2F3F8)),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = null,
                        tint = Color(0xFF111C67),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Antrean Bengkel",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF151B4E)
                    )

                    Spacer(modifier = Modifier.height(2.dp))


                    Text(
                        text = item?.cabang?.nama ?: "Cabang tidak tersedia",
                        fontSize = 12.sp,
                        color = Color(0xFF8B8E9D)
                    )
                }

                StatusBadge(
                    text = if (selesai) "Selesai" else "Dibatalkan",
                    success = selesai
                )

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color(0xFF151B4E)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "0${item?.nomor_antrian}",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF151B4E)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                InfoItem(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.DateRange,
                    title = "Tgl. Kedatangan",
                    value = getDateOnly(item?.tanggal_kedatangan ?: "")
                )

                InfoItem(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.LockClock,
                    title = "Jam Kedatangan",
                    value = "${item?.estimasi_jam} WIB"
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider(
                color = Color(0xFFE8E9F0)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row {

                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF2F3F8)),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Motorcycle,
                        contentDescription = null,
                        tint = Color(0xFF111C67),
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {

                    Text(
                        text = "Kendaraan",
                        fontSize = 11.sp,
                        color = Color(0xFF8B8E9D)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "${item?.merk_motor} ${item?.tipe_motor}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = Color(0xFF151B4E)
                    )
                }
            }
        }
    }
}

@Composable
fun StatusBadge(
    text: String,
    success: Boolean
) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(
                if (success)
                    Color(0xFFE5F8EA)
                else
                    Color(0xFFFFE7E7)
            )
            .padding(
                horizontal = 10.dp,
                vertical = 4.dp
            )
    ) {

        Text(
            text = text,
            color = if (success)
                Color(0xFF2FA84F)
            else
                Color(0xFFE53935),
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun InfoItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    value: String
) {

    Row(
        modifier = modifier
    ) {

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color(0xFFF2F3F8)),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF111C67),
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column {

            Text(
                text = title,
                fontSize = 11.sp,
                color = Color(0xFF8B8E9D)
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = value,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = Color(0xFF151B4E)
            )
        }
    }
}
