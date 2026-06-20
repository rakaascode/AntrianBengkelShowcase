package dev.inteiintel.teduhserviceapp.presentation.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.inteiintel.teduhserviceapp.ui.theme.DimGray

/**
 * Top app bar kustom yang konsisten digunakan di seluruh layar aplikasi.
 *
 * Menampilkan judul di tengah dengan opsional satu ikon aksi di sisi kiri.
 * Jika [icon] dan [onIconClick] tidak diberikan, posisi kiri diisi dengan spacer
 * agar judul tetap rata tengah.
 *
 * Digunakan di hampir semua layar detail sebagai pengganti [androidx.compose.material3.TopAppBar]
 * standar karena mendukung layout yang lebih terkontrol.
 *
 * @param title Teks judul yang ditampilkan di tengah bar.
 * @param icon Vektor ikon opsional (biasanya ikon navigasi kembali).
 * @param onIconClick Callback saat ikon ditekan. Wajib diisi jika [icon] tidak null.
 *
 * @see dev.inteiintel.teduhserviceapp.ui.theme.DimGray
 */
@Composable
fun TopAppBarCostum(
    title: String,
    icon: ImageVector? = null,
    onIconClick: (() -> Unit)? = null
) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(color = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            if (icon != null && onIconClick != null) {
                IconButton(onClick = onIconClick) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "icon"
                    )
                }
            } else {
                Spacer(Modifier.width(48.dp))
            }

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
            )

            Spacer(Modifier.width(48.dp)) // balancing kanan kiri
        }

        HorizontalDivider(thickness = 1.dp, color = DimGray)
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewTopAppBar(){
    TopAppBarCostum("Layanan") {

    }
}