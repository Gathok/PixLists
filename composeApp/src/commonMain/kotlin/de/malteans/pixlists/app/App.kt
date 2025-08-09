package de.malteans.pixlists.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import de.malteans.pixlists.presentation.main.MainScreen
import de.malteans.pixlists.presentation.theme.PixListsTheme

val LocalAppLocalization = compositionLocalOf {
    AppLang.English
}

@Composable
fun App() {
    val currentLanguage = rememberAppLocale()

    CompositionLocalProvider(LocalAppLocalization provides currentLanguage) {
        PixListsTheme {
            MainScreen()
        }
    }
}