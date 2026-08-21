package dev.inteiintel.teduhserviceapp.presentation.main.components

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dev.inteiintel.teduhserviceapp.data.model.ui.StnkResult
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * ViewModel yang menangani proses OCR untuk membaca data dari foto STNK.
 *
 * Menggunakan ML Kit Text Recognition untuk mengekstrak teks dari gambar,
 * kemudian mem-parsing hasilnya menjadi [StnkResult] berisi data kendaraan Yamaha.
 */

class ViewModelOCR : ViewModel() {

    private val recognizer by lazy {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    private val _stnkState = MutableStateFlow<StnkResult?>(null)
    val stnkState = _stnkState

    /**
     * Memproses bitmap foto STNK menggunakan ML Kit OCR dan mem-parsing hasilnya.
     *
     * @param bitmap Gambar STNK yang sudah di-crop ke area frame.
     * @param onResult Callback yang dipanggil dengan [StnkResult] hasil parsing.
     */

    fun processOCR(
        bitmap: Bitmap,
        onResult: (StnkResult) -> Unit
    ) {
        val image = InputImage.fromBitmap(bitmap, 0)

        recognizer.process(image)
            .addOnSuccessListener { result ->

                val parsed = parseStnk(result.text)

                _stnkState.value = parsed
                onResult(parsed)
            }
            .addOnFailureListener {
                Log.e("OCR", "FAILED: ${it.message}")
            }
    }

    /**
     * Memproses bitmap dan mengembalikan teks mentah hasil OCR tanpa parsing.
     *
     * @param bitmap Gambar yang akan diproses.
     * @param onResult Callback dengan teks OCR yang sudah dibersihkan dari newline berlebih.
     */

    fun processTextOnly(
        bitmap: Bitmap,
        onResult: (String) -> Unit
    ) {
        val image = InputImage.fromBitmap(bitmap, 0)

        recognizer.process(image)
            .addOnSuccessListener {

                val cleaned = it.text
                    .replace("\n", " ")
                    .replace(Regex("\\s+"), " ")
                    .trim()

                onResult(cleaned)
            }
            .addOnFailureListener {
                onResult("")
            }
    }

    /**
     * Mem-parsing teks mentah hasil OCR menjadi [StnkResult].
     *
     * Urutan parsing dirancang untuk menghindari konflik antar regex:
     * 1. No Rangka (VIN) — di-parse pertama sebagai anchor
     * 2. No Mesin — alphanumeric, exclude substring VIN
     * 3. Nomor Polisi — exclude substring yang ada di VIN
     * 4. Nama Pemilik — ambil baris setelah label NAMA/PEMILIK, validasi dengan regex nama Indonesia
     * 5. Merk/Tipe — deteksi keyword YAMAHA, ambil baris penuh sebagai tipe
     * 6. Tahun — range 2000–2026
     *
     * @param text Teks mentah hasil ML Kit OCR.
     * @return [StnkResult] berisi data yang berhasil di-parse, field kosong jika tidak terdeteksi.
     */
    
    fun parseStnk(text: String): StnkResult {

        val lines = text.split("\n").map { it.trim() }.filter { it.isNotEmpty() }

        val cleanText = text
            .replace("\n", " ")
            .replace(Regex("\\s+"), " ")
            .uppercase()

        val merkKeywords = listOf("YAMAHA")

        val noRangka = Regex("MH[A-Z0-9]{10,20}")
            .find(cleanText)
            ?.value

        val noMesin = Regex("\\b[A-Z0-9]{7,15}\\b")
            .findAll(cleanText)
            .map { it.value }
            .firstOrNull { candidate ->
                candidate != noRangka &&
                        !(noRangka?.contains(candidate) ?: false) &&
                        candidate.length in 7..15 &&
                        candidate.any { it.isDigit() } &&
                        candidate.any { it.isLetter() } &&
                        !merkKeywords.contains(candidate)
            }

        val plat = Regex("\\b([A-Z]{1,3}\\s?\\d{1,4}\\s?[A-Z]{1,3})\\b")
            .findAll(cleanText)
            .map { it.value.replace(Regex("\\s+"), " ").trim() }
            .firstOrNull { candidate ->
                candidate.length >= 5 &&
                        !(noRangka?.contains(candidate.replace(" ", "")) ?: false)
            }

        val namaRegex = Regex("^[A-Z][a-z]+(?:[ '\\-][A-Z][a-z]+){0,4}$")

        val namaBlacklist = setOf(
            "Yamaha",
            "Bandar", "Lampung", "Jakarta", "Bandung", "Surabaya", "Medan",
            "Jawa", "Barat", "Timur", "Tengah", "Selatan", "Utara",
            "Indonesia", "Republik", "Polisi", "Nomor", "Mesin", "Rangka",
            "Berlaku", "Sampai", "Pajak", "Motor", "Mobil", "Kendaraan"
        )

        val nama = run {
            val labelIndex = lines.indexOfFirst { line ->
                val u = line.uppercase()
                u.contains("NAMA") || u.contains("PEMILIK") ||
                u.startsWith("AN.") || u.startsWith("A.N")
            }

            val fromLabel = if (labelIndex >= 0 && labelIndex + 1 < lines.size) {
                lines[labelIndex + 1].trim().takeIf { namaRegex.matches(it) }
            } else null

            if (fromLabel != null) return@run fromLabel

            lines.firstOrNull { line ->
                val trimmed = line.trim()
                namaRegex.matches(trimmed) &&
                        trimmed.split(" ").none { it in namaBlacklist }
            }?.trim()
        }

        val merkFound = merkKeywords.firstOrNull { cleanText.contains(it) }
        val tipe = run {
            if (merkFound == null) return@run null
            val merkLine = lines.firstOrNull { it.uppercase().contains(merkFound) }
            merkLine?.trim() ?: merkFound
        }

        val tahun = Regex("\\b(20\\d{2})\\b")
            .findAll(cleanText)
            .map { it.value.toInt() }
            .firstOrNull { it in 2000..2026 }
            ?.toString()

        Log.d("OCR_PARSE", "Raw text: $cleanText")
        Log.d("OCR_PARSE", "Plat=$plat | Nama=$nama | Tipe=$tipe | Tahun=$tahun | Rangka=$noRangka | Mesin=$noMesin")

        return StnkResult(
            nama      = nama     ?: "",
            no_polisi = plat     ?: "",
            no_rangka = noRangka ?: "",
            no_mesin  = noMesin  ?: "",
            tipe      = tipe     ?: "",
            tahun     = tahun    ?: ""
        )
    }
}