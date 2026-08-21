package dev.inteiintel.teduhserviceapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

/**
 * Memformat string tanggal ISO 8601 dengan offset ke format yang ramah pengguna.
 *
 * @param time String tanggal dalam format ISO 8601 dengan offset, contoh: `2024-07-15T09:30:00+07:00`.
 * @return String tanggal terformat `"dd MMM yyyy HH:mm"`, atau `"-"` jika parsing gagal.
 */
@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(time: String?): String {
    return try {
        val parsed = OffsetDateTime.parse(time)
        parsed.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"))
    } catch (e: Exception) {
        "-"
    }
}