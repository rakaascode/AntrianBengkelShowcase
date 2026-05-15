package dev.inteiintel.teduhserviceapp.utils.navigation

sealed class Screen(val route: String) {
    object Auth: Screen("auth")
    object Main: Screen("main")

    // Navigation Bottom Object
    object Home: Screen("home")
    object Queues: Screen("queues")
    object Notifications: Screen("notifications")
    object Settings: Screen("settings")

    object DaftarCabang: Screen("daftar_cabang")
    object Riyawat: Screen("riwayat")
    object Pengingat: Screen("pengingat")

    object  DetailCabang: Screen("detail_cabang/{branchId}"){
        fun createRoute(branchId: Int) = "detail_cabang/$branchId"
    }

    object  TambahDataSTNK: Screen("ambil_antrean")


    object ScanStnk : Screen("scan_stnk")

    object DetailAntreanActive: Screen("antrean_aktif")


    object BerhasilAmbilAntrean : Screen("berhasil_ambil")

    object DetailNotification : Screen("detail_notifikasi/{id}"){
        fun createRoute(id: Int?) = "detail_notifikasi/$id"
    }

    object EditProfileScreen: Screen("edit_profile")

    object AmbilAntreanFromBeranda:Screen("ambil_antrean_beranda")

    object DataTersimpan: Screen("data_tersimpan")
    object PilihCabang: Screen("pilih_cabang")
    object KonfirmasiAntrean: Screen("konfirmasi_antrean")




    object TestScreen: Screen("test")
    object HorizontalPager: Screen("pager")
    object DetailScreen: Screen("detail")
    object TakeQueueScreen: Screen("take_queue")
}