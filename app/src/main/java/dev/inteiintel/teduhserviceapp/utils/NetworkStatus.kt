package dev.inteiintel.teduhserviceapp.utils

/**
 * Representasi tipe-safe dari status konektivitas jaringan.
 *
 * Digunakan oleh [NetworkUtils.checkNetwork] untuk memberikan konteks yang lebih kaya
 * dibanding boolean sederhana, sehingga UI dapat menampilkan pesan yang sesuai.
 *
 * @see dev.inteiintel.teduhserviceapp.utils.NetworkUtils
 */
sealed class NetworkStatus {
    /** Jaringan dan internet tersedia. */
    object Available : NetworkStatus()

    /** Tidak ada jaringan yang aktif (WiFi/data mati). */
    object NoConnection : NetworkStatus()

    /** Jaringan aktif tetapi tidak dapat menjangkau internet. */
    object NoInternet : NetworkStatus()
}