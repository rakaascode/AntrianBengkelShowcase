package dev.inteiintel.teduhserviceapp.ui.main.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import kotlinx.coroutines.delay

data class Branch(
    val name: String,
    val lat: Double,
    val lng: Double,
    val distance: String,
    val queue: String,
    val estimate: String
)

@Composable
fun HomeScreen() {

    val branches = listOf(
        Branch("Yamaha Central Lampung", -5.1133, 105.3067, "1.2 km", "A12", "15m"),
        Branch("Lautan Teduh Metro", -5.1200, 105.3100, "2.0 km", "B10", "20m"),
        Branch("Yamaha Bandar Jaya", -5.1300, 105.3200, "3.1 km", "C05", "25m"),
        Branch("Yamaha Trimurjo", -5.1400, 105.3300, "4.0 km", "D02", "30m")
    )


    var selectedLocation by remember {
        mutableStateOf(Point.fromLngLat(105.3067, -5.1133))
    }



    Box(modifier = Modifier.fillMaxSize()) {

        // MAP DUMMY
        MapboxView(selectedLocation)

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .heightIn(max = 260.dp)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(Color(0xFFF5F5F5))
                .padding(vertical = 10.dp, )
        ) {

            Text(
                "Cabang Terdekat",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(8.dp))

            BranchCarousel(branches)
        }
    }
}

@Composable
fun BranchCarousel(branches: List<Branch>) {

    val listState = rememberLazyListState()

    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    LazyRow(
        state = listState,
        flingBehavior = flingBehavior,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
    ) {

        itemsIndexed(branches) { _, branch ->
            BranchCard(branch)
        }
    }
}

@Composable
fun BranchCard(branch: Branch) {
    Box(
        modifier = Modifier
            .width(260.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(14.dp)
    ) {
        Column {

            Text(
                branch.name,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Spacer(Modifier.height(6.dp))

            Text("📍 ${branch.distance}")

            Spacer(Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoBoxSmall("Antrian", branch.queue)
                InfoBoxSmall("Estimasi", branch.estimate)
            }
        }
    }
}

@SuppressLint("Lifecycle")
@Composable
fun MapboxView(location: Point) {

    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    // 🔥 Update camera saat location berubah
    LaunchedEffect(location) {
        mapView.mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(location)
                .zoom(14.5)
                .build()
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            mapView.onStop()
            mapView.onDestroy()
        }
    }

    AndroidView(
        factory = {
            mapView.apply {
                mapboxMap.loadStyle(Style.MAPBOX_STREETS)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun InfoBoxSmall(title: String, value: String) {
    Column(
        modifier = Modifier
            .background(Color(0xFFF1F1F1), RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Text(title, color = Color.Gray)
        Text(value, fontWeight = FontWeight.Bold)
    }
}