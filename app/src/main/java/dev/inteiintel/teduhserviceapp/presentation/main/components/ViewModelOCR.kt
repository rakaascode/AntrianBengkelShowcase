package dev.inteiintel.teduhserviceapp.presentation.main.components

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dev.inteiintel.teduhserviceapp.data.model.ui.StnkResult
import kotlinx.coroutines.flow.MutableStateFlow

class ViewModelOCR : ViewModel() {

    private val recognizer by lazy {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    private val _stnkState = MutableStateFlow<StnkResult?>(null)
    val stnkState = _stnkState

    // =========================
    // FULL OCR
    // =========================
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

    // =========================
    // TEXT ONLY (FOR CROPPING TEST)
    // =========================
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

    // =========================
    // STNK SMART PARSER (INDONESIA RULE BASED)
    // =========================
    fun parseStnk(text: String): StnkResult {

        val cleanText = text
            .replace("\n", " ")
            .replace(Regex("\\s+"), " ")
            .uppercase()

        // =========================
        // 1. NOMOR POLISI (LAMPUNG = BE)
        // =========================
        val plat = Regex("(BE\\s?\\d{1,4}\\s?[A-Z]{1,3})")
            .find(cleanText)
            ?.value

        // =========================
        // 2. NAMA PEMILIK
        // =========================
        val nama = Regex("NAMA PEMILIK\\s*[:\\-]?\\s*([A-Z .]{3,})")
            .find(cleanText)
            ?.groupValues?.get(1)
            ?.trim()

        // =========================
        // 3. MERK (HANYA YAMAHA)
        // =========================
        val tipe = if (cleanText.contains("YAMAHA")) {
            "YAMAHA"
        } else {
            null
        }

        // =========================
        // 4. TAHUN (2010 - 2026)
        // =========================
        val tahun = Regex("\\b(20\\d{2})\\b")
            .findAll(cleanText)
            .map { it.value.toInt() }
            .firstOrNull { it in 2010..2026 }
            ?.toString()

        // =========================
        // 5. NO RANGKA (VIN INDONESIA MH*)
        // =========================
        val noRangka = Regex("MH[A-Z0-9]{10,20}")
            .find(cleanText)
            ?.value

        // =========================
        // 6. NO MESIN (SAFE RANDOM ALPHANUMERIC)
        // =========================
        val noMesin = Regex("\\b[A-Z0-9]{6,15}\\b")
            .findAll(cleanText)
            .map { it.value }
            .firstOrNull { candidate ->
                candidate != noRangka &&
                        candidate.length in 7..15 &&
                        candidate.any { it.isDigit() } &&
                        candidate.any { it.isLetter() }
            }

        return StnkResult(
            nama = nama,
            no_polisi = plat,
            no_rangka = noRangka,
            no_mesin = noMesin,
            tipe = tipe,
            tahun = tahun
        )
    }
}