package com.codelab.basics.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.core.view.ViewCompat
import com.codelab.basics.R
import androidx.compose.material3.Typography
import androidx.compose.ui.unit.sp

// Have to do all this to use non-default font, as well as new defaults
val SazanamiFont = FontFamily(
    Font(R.font.sazanami_gothic, FontWeight.Normal)
)

val SazanamiTypography = Typography(
    headlineMedium = TextStyle(
        fontFamily = SazanamiFont,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = SazanamiFont,
        fontSize = 16.sp
    )
)

// Changed pink to be the new primary colour
// Navy, White, and Chartreuse are now never used
private val DarkColorScheme = darkColorScheme(
    surface = Blue,
    onSurface = Navy,
    primary = Pink,
    onPrimary = Chartreuse,
    background = Color.Black
)

private val LightColorScheme = lightColorScheme(
    surface = Blue,
    onSurface = Color.White,
    primary = Pink,
    onPrimary = Navy
)

@Suppress("DEPRECATION")
@Composable
fun BasicsCodelabTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    // Disabled because it was overriding the colours I wanted to use
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = SazanamiTypography,
        content = content
    )
}