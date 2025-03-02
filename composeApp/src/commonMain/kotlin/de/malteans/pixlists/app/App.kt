package de.malteans.pixlists.app

import androidx.compose.runtime.Composable
import de.malteans.pixlists.presentation.main.MainScreen
import de.malteans.pixlists.presentation.theme.PixListsTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    PixListsTheme {
        MainScreen()
    }
}