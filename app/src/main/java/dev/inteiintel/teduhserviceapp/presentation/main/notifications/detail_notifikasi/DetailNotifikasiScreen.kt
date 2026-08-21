package dev.inteiintel.teduhserviceapp.presentation.main.notifications.detail_notifikasi

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import dev.inteiintel.teduhserviceapp.presentation.main.notifications.getTimeOnly
import dev.inteiintel.teduhserviceapp.presentation.main.queues.getDateOnly
import dev.inteiintel.teduhserviceapp.ui.theme.DarkSlate
import dev.inteiintel.teduhserviceapp.ui.theme.DimGray
import dev.inteiintel.teduhserviceapp.ui.theme.PrimBlue
import dev.inteiintel.teduhserviceapp.ui.theme.SnowWhite
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailNotifikasiScreen(
    onBackClick: () -> Unit = {},
    id: Int,
    viewModel: DetailNotifikacationViewModel = hiltViewModel()
) {

    val detailNotification by viewModel
        .getDataDetailNotification
        .collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDetailNotifications(id)
    }

    val backgroundColor = Color(0xFFF6F7FB)
    val primaryColor = Color(0xFF0B166F)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {

        Box(modifier = Modifier.background(SnowWhite).fillMaxWidth().padding(vertical = 10.dp, horizontal = 10.dp)){
            Row (modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically,horizontalArrangement = Arrangement.Center){
                Text("Detail Notifikasi", fontWeight = FontWeight.Bold, )
            }

            IconButton(onClick = {
                onBackClick()
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null,
                    modifier = Modifier.size(24.dp), tint = DarkSlate)
            }
        }


        HorizontalDivider( thickness = 1.dp, color = DimGray)

        LazyColumn {
            item{
                if (detailNotification == null) {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Spacer(Modifier.height(500.dp))
                        CircularProgressIndicator()
                    }

                    return@item
                }

                val data = detailNotification!!

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(primaryColor),
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                imageVector = Icons.Outlined.NotificationsNone,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.size(14.dp))

                        Column {

                            Text(
                                text = data.judul,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = getTimeOnly(data.created_at),
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    val imageUrl =
                        if (data.gambar_url.isNullOrEmpty())
                            "https://i.pravatar.cc/300"
                        else
                            data.gambar_url

                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp)
                            .clip(RoundedCornerShape(24.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // ===== CONTENT =====
                    Text(
                        text = data.deskripsi,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        lineHeight = 28.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = data.detail,
                        fontSize = 16.sp,
                        color = Color(0xFF4B5563),
                        lineHeight = 28.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    NotificationInfoItem(
                        icon = Icons.Outlined.CalendarMonth,
                        title = "Waktu",
                        value = getDateOnly(data.created_at)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    NotificationInfoItem(
                        icon = Icons.Outlined.LocalOffer,
                        title = "Kategori",
                        value = data.tipe
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    NotificationInfoItem(
                        icon = Icons.Outlined.Info,
                        title = "Cabang ID",
                        value = data.cabang_id ?: "-"
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }

        }
    }


}

@Composable
fun NotificationInfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color(0xFFEFF2FF)),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF0B166F),
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        Column {

            Text(
                text = title,
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateTime(date: String): String {

    return try {

        val zdt = ZonedDateTime.parse(date)
            .withZoneSameInstant(
                ZoneId.of("Asia/Jakarta")
            )

        zdt.format(
            DateTimeFormatter.ofPattern(
                "dd MMM yyyy • HH:mm"
            )
        )

    } catch (e: Exception) {
        date
    }
}