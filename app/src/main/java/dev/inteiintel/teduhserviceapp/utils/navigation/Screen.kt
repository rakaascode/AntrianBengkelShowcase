package dev.inteiintel.teduhserviceapp.utils.navigation

/**
 * Definisi seluruh rute navigasi aplikasi Teduh Service.
 *
 * Setiap `object` merepresentasikan satu layar (screen) dengan [route]-nya masing-masing.
 * Rute yang membutuhkan argumen dinamis memiliki fungsi `createRoute()` untuk membangun
 * path yang benar.
 *
 * Digunakan bersama [AppNavGraph] untuk mendaftarkan dan berpindah antar composable.
 *
 * @see dev.inteiintel.teduhserviceapp.utils.navigation.AppNavGraph
 */
sealed class Screen(val route: String) {

    /** Layar autentikasi (login + onboarding). */
    object Auth : Screen("auth")

    /** Layar utama (shell dengan bottom navigation). */
    object Main : Screen("main")

    // ── Bottom Navigation ──────────────────────────────────────────────────────

    /** Tab Beranda. */
    object Home : Screen("home")

    /** Tab Antrian aktif. */
    object Queues : Screen("queues")

    /** Tab Notifikasi/broadcast. */
    object Notifications : Screen("notifications")

    /** Tab Profil (settings). */
    object Settings : Screen("settings")

    // ── Alur Ambil Antrian ─────────────────────────────────────────────────────

    /** Daftar semua cabang bengkel. */
    object DaftarCabang : Screen("daftar_cabang")

    /** Riwayat antrian yang sudah selesai. */
    object Riyawat : Screen("riwayat")

    /** Pengingat servis kendaraan. */
    object Pengingat : Screen("pengingat")

    /**
     * Detail cabang bengkel dengan argumen `branchId`.
     *
     * Gunakan [createRoute] untuk membangun rute dengan ID yang benar.
     *
     * @see dev.inteiintel.teduhserviceapp.presentation.main.home.detail_cabang.DetailCabangScreen
     */
    object DetailCabang : Screen("detail_cabang/{branchId}") {
        /** Membangun rute dengan [branchId] yang ditentukan. */
        fun createRoute(branchId: Int) = "detail_cabang/$branchId"
    }

    /** Form input data kendaraan untuk ambil antrian baru. */
    object TambahDataSTNK : Screen("ambil_antrean")

    /**
     * Kamera OCR untuk memindai STNK.
     *
     * Hasil scan dikembalikan ke layar sebelumnya melalui `savedStateHandle["stnk_result"]`.
     *
     * @see dev.inteiintel.teduhserviceapp.presentation.main.components.ScanStnkScreen
     */
    object ScanStnk : Screen("scan_stnk")

    /** Detail antrian yang sedang aktif. */
    object DetailAntreanActive : Screen("antrean_aktif")

    /** Halaman konfirmasi sukses setelah antrian berhasil dibuat. */
    object BerhasilAmbilAntrean : Screen("berhasil_ambil")

    /**
     * Detail notifikasi/broadcast dengan argumen `id`.
     *
     * Gunakan [createRoute] untuk membangun rute dengan ID yang benar.
     */
    object DetailNotification : Screen("detail_notifikasi/{id}") {
        /** Membangun rute dengan [id] notifikasi yang ditentukan. */
        fun createRoute(id: Int?) = "detail_notifikasi/$id"
    }

    /** Layar edit profil pengguna. */
    object EditProfileScreen : Screen("edit_profile")

    /** Entry ambil antrian yang dipanggil dari widget beranda (bukan dari tab Queues). */
    object AmbilAntreanFromBeranda : Screen("ambil_antrean_beranda")

    /** Pilih data kendaraan yang sudah tersimpan secara lokal. */
    object DataTersimpan : Screen("data_tersimpan")

    /** Pilih estimasi waktu kedatangan servis. */
    object PilihEstimasi : Screen("pilih_estimasi")

    /** Pilih cabang bengkel dari daftar. */
    object PilihCabang : Screen("pilih_cabang")

    /** Konfirmasi ringkasan data antrian sebelum dikirim ke server. */
    object KonfirmasiAntrean : Screen("konfirmasi_antrean")

    /** Pantau antrian realtime — user memilih cabang lalu melihat status antriannya. */
    object PantauAntrian : Screen("pantau_antrian")

    /** Panduan tips perawatan kendaraan. */
    object Panduan : Screen("panduan")

    /** Detail Promo dan penawaran spesial bengkel. */
    object DetailPromo : Screen("detail_promo")

    // ── Layar Development / Percobaan (tidak dipakai di production) ────────────

    /** @suppress */
    object TestScreen : Screen("test")

    /** @suppress */
    object HorizontalPager : Screen("pager")

    /** @suppress */
    object DetailScreen : Screen("detail")

    /** @suppress */
    object TakeQueueScreen : Screen("take_queue")
}