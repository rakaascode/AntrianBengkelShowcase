package dev.inteiintel.teduhserviceapp

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import dev.inteiintel.teduhserviceapp.utils.navigation.AppNavGraph
import dev.inteiintel.teduhserviceapp.presentation.splash.SplashScreenViewModel
import dev.inteiintel.teduhserviceapp.ui.theme.TeduhServiceAppTheme

/**
 * Activity utama aplikasi, menjadi satu-satunya Activity dalam arsitektur single-Activity.
 *
 * Bertanggung jawab untuk:
 * - Menampilkan splash screen (Android 12+) selama status login dicek.
 * - Menyetel konten Compose melalui [AppNavGraph].
 * - Menerapkan [TeduhServiceAppTheme] secara global.
 *
 * @see dev.inteiintel.teduhserviceapp.utils.navigation.AppNavGraph
 * @see dev.inteiintel.teduhserviceapp.presentation.splash.SplashScreenViewModel
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val splashScreenViewModel: SplashScreenViewModel by lazy {
        ViewModelProvider(this@MainActivity)[SplashScreenViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)

            installSplashScreen().apply {
                setKeepOnScreenCondition { splashScreenViewModel.isSplashScreenVisible.value }
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12+
            window.statusBarColor = Color.WHITE
        }

        // Request runtime permission for notifications on Android 13+ (API 33)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }

        splashScreenViewModel.checkLogin(this)
        setContent {
            TeduhServiceAppTheme {
                Surface (modifier = Modifier.fillMaxSize().fillMaxHeight()){
                    AppNavGraph()
                }
            }
        }
        setTheme(R.style.Theme_TeduhServiceApp)
    }
}

