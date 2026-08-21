package dev.inteiintel.teduhserviceapp.presentation.main.notifications

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import dev.inteiintel.teduhserviceapp.R
import dev.inteiintel.teduhserviceapp.data.model.NotificationData
import dev.inteiintel.teduhserviceapp.ui.theme.BgNotif
import dev.inteiintel.teduhserviceapp.ui.theme.DimGray
import dev.inteiintel.teduhserviceapp.ui.theme.PrimBlue
import dev.inteiintel.teduhserviceapp.ui.theme.SnowWhite
import dev.inteiintel.teduhserviceapp.utils.navigation.Screen
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationsScreen(
    viewModel: NotificationsViewModel = hiltViewModel(),
    navController: NavController,
) {
    val notifications by viewModel.getNotificationData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val unreadCount = notifications.count { !it.isRead }

    val lifecycleOwner = LocalLifecycleOwner.current

    // Start polling saat screen ON_RESUME, stop saat ON_PAUSE
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    viewModel.refresh()      // langsung refresh saat kembali ke screen
                    viewModel.startPolling() // mulai polling
                }
                Lifecycle.Event.ON_PAUSE -> {
                    viewModel.stopPolling()  // hemat baterai saat screen tidak aktif
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val pullState = rememberPullToRefreshState()

    Column(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {

        // ─── Top Bar ──────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Notifikasi",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                // Badge unread count
                if (unreadCount > 0) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .background(PrimBlue, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (unreadCount > 99) "99+" else unreadCount.toString(),
                            fontSize = 10.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        HorizontalDivider(thickness = 1.dp, color = DimGray)

        // ─── Content dengan Pull-to-Refresh ───────────────────────────────────
        PullToRefreshBox(
            isRefreshing = isLoading,
            onRefresh = { viewModel.refresh() },
            state = pullState,
            modifier = Modifier.fillMaxSize()
        ) {
            if (notifications.isEmpty() && !isLoading) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.img_not_found),
                        contentDescription = "not found",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.size(200.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Saat ini kamu belum memiliki notifikasi",
                        fontSize = 12.sp,
                        color = Color(0xFF7B7E8F),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            } else {
                NotificationScreen(
                    notifications = notifications,
                    navController = navController,
                    onReadNotification = { id -> viewModel.markAsRead(id) }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationScreen(
    notifications: List<NotificationData>,
    navController: NavController,
    onReadNotification: (Int) -> Unit
) {

    LazyColumn {

        items(
            items = notifications,
            key = { it.id }
        ) { item ->

            NotificationCard(
                item = item,
                onClick = {

                    onReadNotification(item.id)
                    navController.navigate(Screen.DetailNotification.createRoute(item.id))
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotificationCard(
    item: NotificationData,
    onClick: () -> Unit
) {



    val isRead = item.isRead

    val bgColor =
        if (isRead)
            Color(0xFFF1F1F1)
        else
            BgNotif

    val textColor =
        if (isRead)
            Color.Black
        else
            Color.White

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor)
            .clickable {
                onClick()
            }
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    Color(0xFF1C1F4A),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null,
                tint = Color.White
            )

            if (!isRead) {

                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            Color.Red,
                            CircleShape
                        )
                        .align(Alignment.TopEnd)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = item.judul.toString(),
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            Text(
                text = item.deskripsi.toString(),
                fontSize = 12.sp,
                color = textColor.copy(alpha = 0.8f)
            )
        }

        Text(
            text = getTimeOnly(item.created_at.toString()),
            fontSize = 12.sp,
            color = textColor
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getTimeOnly(date: String): String {

    val zdt = ZonedDateTime.parse(date)
        .withZoneSameInstant(ZoneId.of("Asia/Jakarta"))

    return zdt.format(
        DateTimeFormatter.ofPattern("HH:mm")
    )
}