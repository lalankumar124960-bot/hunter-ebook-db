package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
  primary = BioGreenPrimary,
  onPrimary = BioGreenOnPrimary,
  primaryContainer = BioGreenContainer,
  onPrimaryContainer = BioGreenOnContainer,
  secondary = BioEmeraldSecondary,
  onSecondary = Color.White,
  secondaryContainer = BioEmeraldContainer,
  onSecondaryContainer = BioEmeraldOnContainer,
  tertiary = BioTealTertiary,
  onTertiary = Color.White,
  tertiaryContainer = BioTealContainer,
  onTertiaryContainer = BioTealOnContainer,
  background = BioBackground,
  onBackground = BioOnBackground,
  surface = BioSurface,
  onSurface = BioOnSurface,
  surfaceVariant = BioSurfaceVariant,
  onSurfaceVariant = BioOnSurfaceVariant,
  outline = BioOutline,
  outlineVariant = BioOutlineVariant
)

private val DarkColorScheme = darkColorScheme(
  primary = Color(0xFF81C784),
  onPrimary = Color(0xFF00390F),
  primaryContainer = Color(0xFF1B5E20),
  onPrimaryContainer = Color(0xFFA5D6A7),
  secondary = Color(0xFFA5D6A7),
  onSecondary = Color(0xFF003A12),
  background = Color(0xFF121412),
  onBackground = Color(0xFFE1E3DE),
  surface = Color(0xFF191C19),
  onSurface = Color(0xFFE1E3DE),
  surfaceVariant = Color(0xFF262C26),
  onSurfaceVariant = Color(0xFFC2C9BF)
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep consistent Sap Biology botanical branding
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
