package dev.inteiintel.teduhserviceapp.utils


import android.graphics.Bitmap

data class CropArea(
    val left: Float,
    val top: Float,
    val width: Float,
    val height: Float
)

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