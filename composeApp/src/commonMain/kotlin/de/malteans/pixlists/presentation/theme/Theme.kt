package de.malteans.pixlists.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = SkyBlue,
    secondary = Peach,
    tertiary = Lavender
)

private val LightColorScheme = darkColorScheme(
    primary = SkyBlue,
    secondary = Peach,
    tertiary = Lavender
)

@Composable
fun PixListsTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (useDarkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}