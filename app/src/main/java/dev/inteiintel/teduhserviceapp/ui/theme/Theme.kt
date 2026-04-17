package dev.inteiintel.teduhserviceapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(

    // PRIMARY (brand utama)
    primary = MidnightBlue,
    onPrimary = SnowWhite,
    primaryContainer = DimGray,
    onPrimaryContainer = SnowWhite,
    inversePrimary = DarkOrange,

    // SECONDARY (accent)
    secondary = DarkOrange,
    onSecondary = SnowWhite,
    secondaryContainer = NavajoWhite,
    onSecondaryContainer = DarkSlate,

    // TERTIARY (support color)
    tertiary = GhostWhite,
    onTertiary = DarkSlate,
    tertiaryContainer = DimGray,
    onTertiaryContainer = SnowWhite,

    // BACKGROUND
    background = DarkSlate,
    onBackground = SnowWhite,

    // SURFACE (card, sheet)
    surface = DimGray,
    onSurface = SnowWhite,
    surfaceVariant = GhostWhite,
    onSurfaceVariant = DarkSlate,

    // ERROR
    error = Color(0xFFFF5C5C),
    onError = SnowWhite,
    errorContainer = Color(0xFF8B1E1E),
    onErrorContainer = SnowWhite,

    // OUTLINE
    outline = DimGray,
    outlineVariant = GhostWhite,

    // SURFACE LEVELS (Material 3 depth)
    surfaceTint = MidnightBlue,
    inverseSurface = GhostWhite,
    inverseOnSurface = DarkSlate,

    scrim = Color(0x99000000),

    surfaceBright = GhostWhite,
    surfaceDim = DarkSlate,
    surfaceContainer = DimGray,
    surfaceContainerHigh = MidnightBlue,
    surfaceContainerHighest = MidnightBlue,
    surfaceContainerLow = DarkSlate,
    surfaceContainerLowest = GhostWhite
)

private val LightColorScheme = lightColorScheme(

    // PRIMARY
    primary = MidnightBlue,
    onPrimary = SnowWhite,
    primaryContainer = GhostWhite,
    onPrimaryContainer = DarkSlate,
    inversePrimary = DarkOrange,

    // SECONDARY
    secondary = DarkOrange,
    onSecondary = SnowWhite,
    secondaryContainer = NavajoWhite,
    onSecondaryContainer = DarkSlate,

    // TERTIARY
    tertiary = DimGray,
    onTertiary = SnowWhite,
    tertiaryContainer = GhostWhite,
    onTertiaryContainer = DarkSlate,

    // BACKGROUND
    background = GhostWhite,
    onBackground = DarkSlate,

    // SURFACE
    surface = SnowWhite,
    onSurface = DarkSlate,
    surfaceVariant = NavajoWhite,
    onSurfaceVariant = DimGray,

    // ERROR
    error = Color(0xFFB3261E),
    onError = SnowWhite,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    // OUTLINE
    outline = DimGray,
    outlineVariant = NavajoWhite,

    // SURFACE LEVELS
    surfaceTint = MidnightBlue,
    inverseSurface = DarkSlate,
    inverseOnSurface = SnowWhite,

    scrim = Color(0x66000000),

    surfaceBright = SnowWhite,
    surfaceDim = GhostWhite,
    surfaceContainer = NavajoWhite,
    surfaceContainerHigh = GhostWhite,
    surfaceContainerHighest = SnowWhite,
    surfaceContainerLow = GhostWhite,
    surfaceContainerLowest = Color(0xFFFFFFFF)
)

@Composable
fun TeduhServiceAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColors: Boolean = false,
    content: @Composable ()-> Unit
){

    val colorScheme = when{
        dynamicColors && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if(darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> LightColorScheme
        else -> LightColorScheme
    }

    val shapes = Shapes(
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(12.dp),
        large = RoundedCornerShape(24.dp)
    )

    MaterialTheme(
        typography = typography,
        colorScheme = colorScheme,
        shapes = shapes,
        content = content
    )

}
