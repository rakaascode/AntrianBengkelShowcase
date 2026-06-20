package dev.inteiintel.teduhserviceapp.presentation.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Composable onboarding pager berbasis LazyColumn (komponen percobaan/development).
 *
 * **Catatan:** Implementasi ini belum selesai — item yang dirender di dalam
 * [itemsIndexed] masih di-comment. Fungsionalitas pager onboarding yang aktif
 * digunakan di production ada di [AuthLoginScreen].
 *
 * @param viewModel ViewModel yang menyediakan data onboarding.
 *
 * @see dev.inteiintel.teduhserviceapp.presentation.components.HorizontalPagerViewModel
 * @see dev.inteiintel.teduhserviceapp.presentation.auth.AuthLoginScreen
 */
@Composable
fun HorizontalPager(viewModel: HorizontalPagerViewModel = viewModel()) {
    val data = viewModel.getDataOnBoardingModel.collectAsState()
    LazyColumn {
        itemsIndexed(data.value) { index, item ->
//                Image(painter = painterResource(item.Image), contentDescription = null, Modifier.aspectRatio(2f))
//                Spacer(Modifier.height(12.dp))
//                Text(text = stringResource(item.title))
//                Spacer(Modifier.height(12.dp))
//                Text(text = stringResource(item.subtitle))
        }
    }
}