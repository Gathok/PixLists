package de.malteans.pixlists.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import de.malteans.pixlists.R

@Composable
actual fun AppIconPainter(): Painter? {
    return painterResource(id = R.mipmap.ic_launcher_foreground)
}