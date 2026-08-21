package dev.inteiintel.teduhserviceapp.utils.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.inteiintel.teduhserviceapp.presentation.main.MainScreen
import dev.inteiintel.teduhserviceapp.data.local.TokenManager
import dev.inteiintel.teduhserviceapp.data.mapper.toRequest
import dev.inteiintel.teduhserviceapp.data.model.AntreanActiveData
import dev.inteiintel.teduhserviceapp.data.model.Branch
import dev.inteiintel.teduhserviceapp.data.model.CreateAntrianRequest
import dev.inteiintel.teduhserviceapp.data.model.CreateAntrianResponse
import dev.inteiintel.teduhserviceapp.data.model.ui.KonfirmasiAntreanUiModel
import dev.inteiintel.teduhserviceapp.presentation.auth.AuthLoginScreen
import dev.inteiintel.teduhserviceapp.presentation.main.components.ScanStnkScreen
import dev.inteiintel.teduhserviceapp.presentation.main.home.ambil_antrean.DataKendaraanScreen
import dev.inteiintel.teduhserviceapp.presentation.main.home.ambil_antrean.berhasil_ambil.SuccessQueueScreen
import dev.inteiintel.teduhserviceapp.presentation.main.home.ambil_antrean.data_tersimpan.DataTersimpanViewModel
import dev.inteiintel.teduhserviceapp.presentation.main.home.ambil_antrean.data_tersimpan.PilihDataKendaraanScreen
import dev.inteiintel.teduhserviceapp.presentation.main.home.ambil_antrean.konfirmasi_antrean.KonfirmasiAntreanScreen
import dev.inteiintel.teduhserviceapp.presentation.main.home.ambil_antrean.pilih_cabang.PilihCabangScreen
import dev.inteiintel.teduhserviceapp.presentation.main.home.ambil_antrean.pilih_estimasi.PilihEstimasiScreen
import dev.inteiintel.teduhserviceapp.presentation.main.home.ambil_antrean.tambah_data.SavedAntrianViewModel
import dev.inteiintel.teduhserviceapp.presentation.main.home.ambil_antrean.tambah_data.TambahDataScreen
import dev.inteiintel.teduhserviceapp.presentation.main.home.ambil_antrean.tambah_data.TambahDataScreenViewModel
import dev.inteiintel.teduhserviceapp.presentation.main.home.detail_cabang.DetailCabangScreen
import dev.inteiintel.teduhserviceapp.presentation.main.home.detail_cabang.daftar_cabang.DaftarCabangScreen
import dev.inteiintel.teduhserviceapp.presentation.main.home.detail_cabang.daftar_cabang.DaftarCabangViewModel
import dev.inteiintel.teduhserviceapp.presentation.main.home.history.HistoryScreen
import dev.inteiintel.teduhserviceapp.presentation.main.home.panduan.PanduanScreen
import dev.inteiintel.teduhserviceapp.presentation.main.home.pantau_antrian.PantauAntrianScreen
import dev.inteiintel.teduhserviceapp.presentation.main.home.promo.DetailPromoScreen
import dev.inteiintel.teduhserviceapp.presentation.main.home.reminder.ReminderScreen
import dev.inteiintel.teduhserviceapp.presentation.main.notifications.detail_notifikasi.DetailNotifikasiScreen
import dev.inteiintel.teduhserviceapp.presentation.main.profile.edit_profile.EditProfileScreen
import dev.inteiintel.teduhserviceapp.presentation.main.queues.detail_antrean.DetailAntreanScreen
import dev.inteiintel.teduhserviceapp.presentation.main.queues.getDateOnly

/**
 * Graf navigasi utama aplikasi Teduh Service.
 *
 * Mengecek status login via [TokenManager] saat pertama kali di-compose, lalu menentukan
 * `startDestination` apakah ke [Screen.Auth] atau [Screen.Main].
 *
 * Semua rute aplikasi (termasuk alur ambil antrian dan detail layar) didaftarkan di sini.
 * Data antar layar dikirim melalui `savedStateHandle` untuk menghindari serialisasi Parcelable
 * yang tidak perlu di setiap transition.
 *
 * **Alur Ambil Antrian:**
 * ```
 * TambahDataSTNK ──scan──> ScanStnk
 *                              │ (kembali dengan stnk_result)
 * TambahDataSTNK ──lanjut──> PilihCabang ──> KonfirmasiAntrean ──> BerhasilAmbilAntrean
 *
 * DataTersimpan  ──pilih──> PilihEstimasi ──> PilihCabang ──> KonfirmasiAntrean
 * ```
 *
 * @see dev.inteiintel.teduhserviceapp.utils.navigation.Screen
 * @see dev.inteiintel.teduhserviceapp.data.local.TokenManager
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph() {

    var startDestination by remember { mutableStateOf<String?>(null) }
    val context= LocalContext.current


    LaunchedEffect(Unit) {
        val isLogin = TokenManager(context).isLoggedIn()
        startDestination = if (isLogin) {
            Screen.Main.route
        } else {
            Screen.Auth.route
        }
    }

    if (startDestination != null) {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = startDestination!!
        ) {
            composable(Screen.Auth.route) {
                AuthLoginScreen(navController = navController)
            }
            composable(Screen.Main.route) {
                MainScreen(rootNavController = navController)
            }

            composable(Screen.DaftarCabang.route) {
                DaftarCabangScreen(navController)
            }

            composable (Screen.DetailCabang.route, arguments = listOf(
                navArgument("branchId"){ type = NavType.IntType }
            )){ backStackEntry ->
                val branchId = backStackEntry.arguments?.getInt("branchId") ?: 0
                DetailCabangScreen(navController = navController, branchId = branchId)

            }





            composable(Screen.DetailAntreanActive.route) {

                val data = navController
                    .previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<AntreanActiveData>("antrean")

                data?.let {
                    DetailAntreanScreen(
                        data = it,
                        onKembaliClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }


            composable(Screen.ScanStnk.route) {

                ScanStnkScreen(
                    onResult = { result ->

                        // Set ke currentBackStackEntry (TambahDataSTNK) bukan previousBackStackEntry
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("stnk_result", result)

                        navController.popBackStack()
                    }
                )
            }

            composable (Screen.DetailNotification.route, arguments = listOf(
                navArgument("id"){ type = NavType.IntType }
            )){ backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: 0
                DetailNotifikasiScreen( id = id, onBackClick = {
                    navController.popBackStack()
                })

            }


            composable(Screen.EditProfileScreen.route){
                EditProfileScreen(navController = navController)
            }

            composable (Screen.Pengingat.route){
                ReminderScreen(navController = navController)
            }

            composable (Screen.Riyawat.route){
                HistoryScreen(navController= navController)
            }

            composable(Screen.PantauAntrian.route) {
                PantauAntrianScreen(navController = navController)
            }

            composable(Screen.Panduan.route) {
                PanduanScreen(navController = navController)
            }

            composable(Screen.DetailPromo.route) {
                DetailPromoScreen(navController = navController)
            }

            composable(Screen.AmbilAntreanFromBeranda.route) {
                DataKendaraanScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onAddVehicleClick = {
                        navController.navigate(Screen.TambahDataSTNK.route)

                    },
                    onSavedVehicleClick = {
                        navController.navigate(Screen.DataTersimpan.route)
                    }
                )
            }

            composable(Screen.DataTersimpan.route) {

                val viewModel: DataTersimpanViewModel = hiltViewModel()
                val kendaraanList = viewModel.list.collectAsState().value

                PilihDataKendaraanScreen(
                    kendaraanList = kendaraanList,
                    onBackClick = { navController.popBackStack() },
                    onPilihKendaraan = { entity ->

                        val request = entity.toRequest()

                        // Simpan request sementara, arahkan ke PilihEstimasi dulu
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("form_data", request)

                        navController.navigate(Screen.PilihEstimasi.route)
                    }
                )
            }

            composable(Screen.PilihEstimasi.route) {

                val formData =
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.get<CreateAntrianRequest>("form_data")

                if (formData != null) {
                    PilihEstimasiScreen(
                        request = formData,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onLanjutClick = { requestWithEstimasi ->

                            navController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.set("form_data", requestWithEstimasi)

                            navController.navigate(Screen.PilihCabang.route)
                        }
                    )
                }
            }

            composable (Screen.TambahDataSTNK.route){

                val savedAntrianViewModel: SavedAntrianViewModel = hiltViewModel()

                // Baca hasil scan OCR dari savedStateHandle (diset oleh route ScanStnk)
                val currentEntry = navController.currentBackStackEntry
                val stnkResult = currentEntry
                    ?.savedStateHandle
                    ?.get<dev.inteiintel.teduhserviceapp.data.model.ui.StnkResult>("stnk_result")

                TambahDataScreen(
                    initialStnkResult = stnkResult,

                    onCancelClick = {
                        navController.popBackStack()
                    },

                    onClickScanOCR = {
                        navController.navigate(Screen.ScanStnk.route)
                    },

                    onNextScreenClick = { req ->

                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("form_data", req)

                        navController.navigate(Screen.PilihCabang.route)
                    },

                    onSaveToLocal = { req ->
                        savedAntrianViewModel.saveToLocal(req)
                    }

                )
            }

            composable (Screen.PilihCabang.route){

                val daftarCabangViewModel: DaftarCabangViewModel = hiltViewModel()

                val dataCabang = daftarCabangViewModel.cabang.collectAsState().value.filterNotNull()

                val formData =
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.get<CreateAntrianRequest>("form_data")



                LaunchedEffect(Unit) {
                    daftarCabangViewModel.loadDataCabang()
                }

                PilihCabangScreen(
                    cabangList = dataCabang,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onPilihCabang = { cabang ->

                        val finalRequest = formData?.copy(
                            cabang_id = cabang.id
                        )

                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("final_request", finalRequest )

                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("selected_cabang", cabang)

                        navController.navigate(Screen.KonfirmasiAntrean.route)
                    }
                )
            }

            composable(Screen.KonfirmasiAntrean.route) {

                val viewModel: TambahDataScreenViewModel = hiltViewModel()

                val isLoading = viewModel.isLoading.collectAsState().value
                val success = viewModel.antreanState.collectAsState().value
                val error = viewModel.errorMessage.collectAsState().value

                val finalRequest =
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.get<CreateAntrianRequest>("final_request")

                val selectedCabang =
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.get<Branch>("selected_cabang")



                if (finalRequest != null && selectedCabang != null) {

                    val uiData = KonfirmasiAntreanUiModel(
                        namaCabang = selectedCabang.nama,
                        alamatCabang = selectedCabang.alamat,
                        namaPemilik = finalRequest.nama_pemilik,
                        nomorPolisi = finalRequest.no_polisi,
                        merkType = "${finalRequest.merk_motor} ${finalRequest.tipe_motor}"
                    )

                    LaunchedEffect(success) {
                        success?.let {

                            // simpan response sukses
                            navController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.set("success_data", it)

                            // SIMPAN CABANG JUGA
                            navController.currentBackStackEntry
                                ?.savedStateHandle
                                ?.set("selected_cabang", selectedCabang)


                            navController.navigate(Screen.BerhasilAmbilAntrean.route)

                            viewModel.resetState()
                        }
                    }

                    KonfirmasiAntreanScreen(

                        data = uiData,

                        request = finalRequest,

                        isLoading = isLoading,

                        onBackClick = {
                            navController.popBackStack()
                        },

                        onBatalClick = {
                            navController.navigate(Screen.Main.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        },

                        onAmbilAntreanClick = {
                            viewModel.createAntrean(it)
                        }
                    )
                }
            }

            composable(Screen.BerhasilAmbilAntrean.route) {

                val successData =
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.get<CreateAntrianResponse>("success_data")

                val selectedCabang =
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.get<Branch>("selected_cabang")

                successData?.let { data ->

                    SuccessQueueScreen(
                        queueNumber = data.data.nomor_display ?: "A-%03d".format(data.data.nomor_antrian),
                        estDate = "${getDateOnly(data.data.tanggal_kedatangan)} - ${data.data.estimasi_jam}",

                        vehicle = "${data.data.no_polisi} - ${data.data.merk_motor} ${data.data.tipe_motor}",

                        branch = selectedCabang?.nama ?: "Cabang tidak ditemukan",

                        onBackHome = {
                            navController.navigate(Screen.Main.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}