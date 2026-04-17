package dev.inteiintel.teduhserviceapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.inteiintel.teduhserviceapp.R
import kotlinx.coroutines.time.delay

@Composable
fun HorizontalPager(viewModel: HorizontalPagerViewModel = viewModel()){
        val data = viewModel.getDataOnBoarding.collectAsState()
        LazyColumn {
            itemsIndexed (data.value){ index, item ->
//                Image(painter = painterResource(item.Image), contentDescription = null, Modifier.aspectRatio(2f))
//                Spacer(Modifier.height(12.dp))
//                Text(text = stringResource(item.title))
//                Spacer(Modifier.height(12.dp))
//                Text(text = stringResource(item.subtitle))
            }
        }
}