package de.malteans.pixlists.presentation.main.util

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NavListHeader(
    onLongClick: () -> Unit,
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .combinedClickable(
                onClick = { },
                onLongClick = onLongClick
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .height(42.dp)
            ) {
//                Image (
//                    painter = painterResource(id = R.mipmap.ic_launcher_foreground),
//                    contentDescription = "Menu",
//                    contentScale = ContentScale.Fit,
//                    modifier = Modifier
//                        .scale(1.5f),
//                )
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Menu",
                    modifier = Modifier.scale(1.5f),
                )
            }
        }
        Column {
            Text(
                text = "PixLists",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
        }

    }
}