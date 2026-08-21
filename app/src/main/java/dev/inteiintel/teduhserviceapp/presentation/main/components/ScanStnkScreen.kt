package dev.inteiintel.teduhserviceapp.presentation.main.components

import android.Manifest
import android.R.attr.textSize
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.inteiintel.teduhserviceapp.data.model.ui.StnkResult
import dev.inteiintel.teduhserviceapp.utils.CropArea
import dev.inteiintel.teduhserviceapp.utils.cropBitmap
import java.io.File
import android.graphics.Paint
import androidx.compose.ui.graphics.nativeCanvas
import androidx.navigation.NavHostController


/**
 * Screen kamera untuk memindai STNK menggunakan ML Kit OCR.
 *
 * Menampilkan live preview kamera dengan overlay frame STNK.
 * Saat tombol scan ditekan, gambar di-crop ke area frame sebelum diproses OCR.
 *
 * @param onResult Callback yang dipanggil dengan hasil parsing [StnkResult] setelah OCR selesai.
 * @param viewModel ViewModel yang menangani proses OCR, default menggunakan [ViewModelOCR].
 */
@Composable
fun ScanStnkScreen(
    onResult: (StnkResult) -> Unit,
    viewModel: ViewModelOCR = viewModel()
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { hasPermission = it }

    LaunchedEffect(Unit) {
        if (!hasPermission) launcher.launch(Manifest.permission.CAMERA)
    }

    val previewView = remember { PreviewView(context) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }

    LaunchedEffect(hasPermission) {
        if (!hasPermission) return@LaunchedEffect

        val cameraProvider = ProcessCameraProvider.getInstance(context).get()

        val preview = Preview.Builder().build()
        preview.setSurfaceProvider(previewView.surfaceProvider)

        imageCapture = ImageCapture.Builder().build()

        cameraProvider.unbindAll()

        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            CameraSelector.DEFAULT_BACK_CAMERA,
            preview,
            imageCapture
        )
    }

    Box(Modifier.fillMaxSize()) {

        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        CanvasHelperStnk()

        Button(
            onClick = {

                val file = File(context.cacheDir, "${System.currentTimeMillis()}.jpg")

                val options = ImageCapture.OutputFileOptions.Builder(file).build()

                imageCapture?.takePicture(
                    options,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {

                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {

                            val raw = BitmapFactory.decodeFile(file.absolutePath)

                            val cropArea = CropArea(
                                left   = 0.06f,
                                top    = 0.22f,
                                width  = 0.88f,
                                height = 0.88f * 0.68f
                            )
                            val bitmap = cropBitmap(raw, cropArea)

                            viewModel.processOCR(bitmap) { result ->
                                onResult(result)
                            }
                        }

                        override fun onError(exception: ImageCaptureException) {
                            exception.printStackTrace()
                        }
                    }
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
        ) {
            Text("📸 Scan STNK")
        }
    }
}

/**
 * Model data untuk mendefinisikan posisi dan tinggi setiap baris field pada overlay STNK.
 *
 * @property name Nama label field yang ditampilkan di kolom kiri overlay.
 * @property topPercent Posisi vertikal baris relatif terhadap tinggi frame STNK (0.0–1.0).
 * @property heightPercent Tinggi baris relatif terhadap tinggi frame STNK (0.0–1.0).
 */
data class FieldRect(
    val name: String,
    val topPercent: Float,
    val heightPercent: Float
)

/**
 * Composable yang menggambar overlay kamera berbentuk frame STNK.
 *
 * Terdiri dari:
 * - Overlay gelap di luar area STNK
 * - Frame STNK dengan border putih
 * - Garis pemisah kolom label dan nilai
 * - Label field (Nomor Polisi, Nama Pemilik, dst.) di kolom kiri
 * - Kotak area OCR di kolom kanan untuk setiap field
 */
@Composable
fun CanvasHelperStnk() {

    val fields = listOf(

        FieldRect("Nomor Polisi", 0.03f, 0.09f),
        FieldRect("Nama Pemilik", 0.14f, 0.09f),

        FieldRect("Merk / Type", 0.50f, 0.08f),
        FieldRect("Tahun", 0.62f, 0.08f),

        FieldRect("No Rangka", 0.72f, 0.07f),
        FieldRect("No Mesin", 0.80f, 0.07f)
    )

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {

        drawRect(Color.Black.copy(alpha = 0.5f))

        val rectWidth = size.width * 0.88f
        val rectHeight = rectWidth * 0.68f

        val left = (size.width - rectWidth) / 2
        val top = (size.height - rectHeight) / 3

        drawRoundRect(
            color = Color.Transparent,
            topLeft = Offset(left, top),
            size = Size(rectWidth, rectHeight),
            cornerRadius = CornerRadius(24f, 24f),
            blendMode = BlendMode.Clear
        )

        drawRoundRect(
            color = Color.White,
            topLeft = Offset(left, top),
            size = Size(rectWidth, rectHeight),
            cornerRadius = CornerRadius(24f, 24f),
            style = Stroke(width = 4f)
        )

        val dividerX = left + (rectWidth * 0.38f)

        drawLine(
            color = Color.White,
            start = Offset(dividerX, top),
            end = Offset(dividerX, top + rectHeight),
            strokeWidth = 2f
        )

        val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.WHITE
            textSize = 26f
            isFakeBoldText = true
        }

        fields.forEach { field ->

            val rowTop = top + (rectHeight * field.topPercent)
            val rowHeight = rectHeight * field.heightPercent

            val labelX = left + 30f
            val labelY = rowTop + rowHeight * 0.7f

            drawContext.canvas.nativeCanvas.drawText(
                field.name,
                labelX,
                labelY,
                labelPaint
            )

            val valueLeft = dividerX + 12f
            val valueWidth = rectWidth * 0.52f

            drawRoundRect(
                color = Color.White,
                topLeft = Offset(valueLeft, rowTop),
                size = Size(valueWidth, rowHeight),
                cornerRadius = CornerRadius(10f, 10f),
                style = Stroke(width = 2f)
            )
        }
    }
}

/**
 * Composable overlay kamera sederhana tanpa field label.
 *
 * Menampilkan frame transparan dengan overlay gelap untuk scanning dokumen portrait.
 * Digunakan sebagai alternatif [CanvasHelperStnk] untuk kasus tanpa label field.
 */
@Composable
fun Scan(){
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(
            color = Color.Black.copy(alpha = 0.5f)
        )

        val rectWidth = size.width * 0.85f
        val rectHeight = rectWidth * 1.8f

        val left = (size.width - rectWidth) / 2
        val top = (size.height - rectHeight) / 4

        drawRoundRect(
            color = Color.Transparent,
            topLeft = Offset(left, top),
            size = Size(rectWidth, rectHeight),
            cornerRadius = CornerRadius(24f, 24f),
            blendMode = BlendMode.Clear
        )

        drawRoundRect(
            color = Color.White,
            topLeft = Offset(left, top),
            size = Size(rectWidth, rectHeight),
            cornerRadius = CornerRadius(24f, 24f),
            style = Stroke(width = 4f)
        )
    }
}

/**
 * Composable section untuk memilih dan mengunggah foto STNK dari galeri.
 *
 * Membuka image picker saat di-tap, memproses gambar melalui OCR,
 * lalu memanggil [onResult] dengan [StnkResult] yang telah diparsing.
 *
 * @param onResult Callback yang dipanggil dengan hasil parsing [StnkResult].
 */
@Composable
fun SectionScan(
    onResult: (StnkResult) -> Unit
) {

    val context = LocalContext.current

    val viewModel = remember {
        ViewModelOCR()
    }

    val stnkResult by viewModel.stnkState.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->

        uri ?: return@rememberLauncherForActivityResult

        val inputStream =
            context.contentResolver.openInputStream(uri)

        val bitmap = BitmapFactory.decodeStream(inputStream)

        viewModel.processOCR(bitmap) { result ->

            onResult(result)
        }
    }

    LaunchedEffect(stnkResult) {

        stnkResult?.let {
            onResult(it)
        }
    }

    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                "Pindai STNK",
                fontWeight = FontWeight.Bold
            )

            Text(
                "Lebih Cepat",
                fontSize = 12.sp,
                color = Color(0xFF5C6BC0)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clickable {
                    launcher.launch("image/*")
                },
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text("📷")
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Unggah Foto STNK",
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Tap untuk upload foto",
                    color = Color.Gray
                )
            }
        }
    }
}


@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun PreviewScan(){
    CanvasHelperStnk()
}