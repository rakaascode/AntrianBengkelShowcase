package dev.inteiintel.teduhserviceapp.utils


import android.graphics.Bitmap

/**
 * Mendefinisikan area crop relatif terhadap dimensi bitmap (0.0–1.0).
 *
 * Digunakan oleh [cropBitmap] untuk memotong gambar kamera ke area frame STNK
 * sebelum diproses oleh OCR.
 *
 * @property left Batas kiri dalam proporsi lebar bitmap.
 * @property top Batas atas dalam proporsi tinggi bitmap.
 * @property width Lebar area crop dalam proporsi lebar bitmap.
 * @property height Tinggi area crop dalam proporsi tinggi bitmap.
 *
 * @see dev.inteiintel.teduhserviceapp.presentation.main.components.ScanStnkScreen
 */
data class CropArea(
    val left: Float,
    val top: Float,
    val width: Float,
    val height: Float
)

/**
 * Memotong bitmap berdasarkan area proporsi yang diberikan oleh [CropArea].
 *
 * Koordinat pixel dihitung dari dimensi bitmap asli dikali nilai proporsi.
 *
 * @param bitmap Bitmap sumber yang akan dipotong.
 * @param area Definisi area crop dalam koordinat relatif (0.0–1.0).
 * @return Bitmap baru hasil crop yang siap diproses oleh ML Kit OCR.
 *
 * @see dev.inteiintel.teduhserviceapp.presentation.main.components.ViewModelOCR.processOCR
 */
fun cropBitmap(
    bitmap: Bitmap,
    area: CropArea
): Bitmap {

    val x = (bitmap.width * area.left).toInt()
    val y = (bitmap.height * area.top).toInt()

    val w = (bitmap.width * area.width).toInt()
    val h = (bitmap.height * area.height).toInt()

    return Bitmap.createBitmap(
        bitmap,
        x,
        y,
        w,
        h
    )
}