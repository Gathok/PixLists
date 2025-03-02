package de.malteans.pixlists.app

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import de.malteans.pixlists.presentation.main.MainScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        MainScreen()
    }
}