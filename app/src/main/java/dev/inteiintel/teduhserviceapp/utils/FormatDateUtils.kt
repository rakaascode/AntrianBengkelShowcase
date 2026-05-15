package dev.inteiintel.teduhserviceapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(time: String?): String {
    return try {
        val parsed = OffsetDateTime.parse(time)
        parsed.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"))
    } catch (e: Exception) {
        "-"
    }
}