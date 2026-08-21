package dev.inteiintel.teduhserviceapp.ui.theme


import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Light Mode",
    showBackground = true,
    showSystemUi = true,
    device = "id:pixel_7"
)          // <- ini preview #1
@Preview(
    name = "Dark Mode",
    showBackground = true,
    showSystemUi = true,
    device = "id:pixel_7",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)  // <- ini preview #2

annotation class ThemePreviews