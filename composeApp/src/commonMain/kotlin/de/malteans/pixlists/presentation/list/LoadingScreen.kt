package de.malteans.pixlists.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.malteans.pixlists.presentation.util.CustomTopBar

@Composable
fun LoadingScreen(openDrawer: () -> Unit) {
    Scaffold (
        topBar = {
            CustomTopBar(
                title = {  },
                actions = {
                    IconButton(
                        onClick = { }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Add Entry",
                            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                        )
                    }
                },
                openDrawer = openDrawer
            )
        }
    ) { pad ->
        Box (
            modifier = Modifier
                .padding(pad)
                .padding(start = 8.dp, end = 4.dp, bottom = 16.dp)
        ) {
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Loading...")
            }
        }
    }
}