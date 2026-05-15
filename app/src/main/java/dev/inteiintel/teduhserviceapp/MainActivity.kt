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

        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12+
            window.statusBarColor = Color.WHITE

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

