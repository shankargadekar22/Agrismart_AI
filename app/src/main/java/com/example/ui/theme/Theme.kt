package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
  primary = AgriPrimaryDark,
  onPrimary = AgriOnPrimaryDark,
  primaryContainer = AgriPrimaryContainerDark,
  onPrimaryContainer = AgriOnPrimaryContainerDark,
  secondary = AgriSecondaryDark,
  onSecondary = AgriOnSecondaryDark,
  secondaryContainer = AgriSecondaryContainerDark,
  onSecondaryContainer = AgriOnSecondaryContainerDark,
  tertiary = AgriTertiaryDark,
  onTertiary = AgriOnTertiaryDark,
  tertiaryContainer = AgriTertiaryContainerDark,
  onTertiaryContainer = AgriOnTertiaryContainerDark,
  background = AgriBackgroundDark,
  onBackground = AgriOnBackgroundDark,
  surface = AgriSurfaceDark,
  onSurface = AgriOnSurfaceDark,
  surfaceVariant = AgriSurfaceVariantDark,
  onSurfaceVariant = AgriOnSurfaceVariantDark
)

private val LightColorScheme = lightColorScheme(
  primary = AgriPrimaryLight,
  onPrimary = AgriOnPrimaryLight,
  primaryContainer = AgriPrimaryContainerLight,
  onPrimaryContainer = AgriOnPrimaryContainerLight,
  secondary = AgriSecondaryLight,
  onSecondary = AgriOnSecondaryLight,
  secondaryContainer = AgriSecondaryContainerLight,
  onSecondaryContainer = AgriOnSecondaryContainerLight,
  tertiary = AgriTertiaryLight,
  onTertiary = AgriOnTertiaryLight,
  tertiaryContainer = AgriTertiaryContainerLight,
  onTertiaryContainer = AgriOnTertiaryContainerLight,
  background = AgriBackgroundLight,
  onBackground = AgriOnBackgroundLight,
  surface = AgriSurfaceLight,
  onSurface = AgriOnSurfaceLight,
  surfaceVariant = AgriSurfaceVariantLight,
  onSurfaceVariant = AgriOnSurfaceVariantLight
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // We prefer the agricultural bespoke theme for brand identity
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
