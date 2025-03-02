package de.malteans.pixlists.presentation.util

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    title: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    openDrawer: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = title,
        actions = actions,
        navigationIcon = {
            androidx.compose.material3.IconButton(
                onClick = {
                    openDrawer()
                }
            ) {
                androidx.compose.material3.Surface(
                    shape = RoundedCornerShape(50),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                    contentColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
                ) {
//                    Image (
//                        painter = painterResource(id = R.mipmap.ic_launcher_foreground),
//                        contentDescription = "Menu",
//                        contentScale = ContentScale.Fit,
//                        modifier = Modifier
//                            .scale(1.5f)
//                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors().copy(
            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
        ),
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 16.dp, bottomEnd = 16.dp))
    )
}