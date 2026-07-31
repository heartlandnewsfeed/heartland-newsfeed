package com.heartlandnewsfeed.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkMode
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
import androidx.core.view.WindowCompat

// Heartland Newsfeed Brand Colors
private val PrimaryHeartland = Color(0xFF2E5090)      // Deep Blue
private val SecondaryHeartland = Color(0xFFE74C3C)    // Red
private val TertiaryHeartland = Color(0xFF27AE60)     // Green
private val SurfaceHeartland = Color(0xFFF8F9FA)
private val BackgroundHeartland = Color(0xFFFFFFFF)

private val PrimaryContainerHeartland = Color(0xFF3D5A9C)
private val OnPrimaryHeartland = Color(0xFFFFFFFF)

// Dark theme colors
private val PrimaryDark = Color(0xFF5B8AC5)
private val SecondaryDark = Color(0xFFFF7A6D)
private val TertiaryDark = Color(0xFF5DD99E)
private val SurfaceDark = Color(0xFF1F1F1F)
private val BackgroundDark = Color(0xFF121212)

private val lightColorScheme = lightColorScheme(
    primary = PrimaryHeartland,
    onPrimary = OnPrimaryHeartland,
    primaryContainer = PrimaryContainerHeartland,
    onPrimaryContainer = Color(0xFF0A2351),
    secondary = SecondaryHeartland,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFDDEDB),
    onSecondaryContainer = Color(0xFF640B15),
    tertiary = TertiaryHeartland,
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFDEF8EC),
    onTertiaryContainer = Color(0xFF005A36),
    error = Color(0xFFB3261E),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),
    background = BackgroundHeartland,
    onBackground = Color(0xFF1C1B1F),
    surface = SurfaceHeartland,
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454E),
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC7D0),
    scrim = Color(0xFF000000),
)

private val darkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = Color(0xFF0A2351),
    primaryContainer = Color(0xFF1F3D6F),
    onPrimaryContainer = Color(0xFFD6E3FF),
    secondary = SecondaryDark,
    onSecondary = Color(0xFF5D0B12),
    secondaryContainer = Color(0xFF821B24),
    onSecondaryContainer = Color(0xFFFFDEDB),
    tertiary = TertiaryDark,
    onTertiary = Color(0xFF00361E),
    tertiaryContainer = Color(0xFF035038),
    onTertiaryContainer = Color(0xFFDEF8EC),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC),
    background = BackgroundDark,
    onBackground = Color(0xFFE6E1E6),
    surface = SurfaceDark,
    onSurface = Color(0xFFE6E1E6),
    surfaceVariant = Color(0xFF49454E),
    onSurfaceVariant = Color(0xFFCAC7D0),
    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454E),
    scrim = Color(0xFF000000),
)

@Composable
fun HeartlandTheme(
    darkTheme: Boolean = isSystemInDarkMode(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> darkColorScheme
        else -> lightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view)?.isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = HeartlandTypography,
        content = content
    )
}
