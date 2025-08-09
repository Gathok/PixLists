package de.malteans.pixlists.presentation.manageColors

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.malteans.pixlists.domain.PixColor
import de.malteans.pixlists.presentation.components.CustomDialog
import de.malteans.pixlists.presentation.components.CustomTopBar
import de.malteans.pixlists.presentation.manageColors.components.ColorDialog
import de.malteans.pixlists.presentation.manageColors.components.ColorItem
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import pixlists.composeapp.generated.resources.Res
import pixlists.composeapp.generated.resources.delete_unused_colors
import pixlists.composeapp.generated.resources.delete_unused_colors_desc
import pixlists.composeapp.generated.resources.manage_colors

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ManageColorsScreen(
    viewModel: ManageColorsViewModel = koinViewModel(),
    openDrawer: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

//    BackHandler {
//        viewModel.undo()
//    }

    var showColorDialog by remember { mutableStateOf(false) }
    var colorToEdit by remember { mutableStateOf<PixColor?>(null) }

    if (showColorDialog) {
        ColorDialog(
            onDismiss = {
                showColorDialog = false
                colorToEdit = null
            },
            onFinish = { newName, newRgb, isEdit ->
                if (isEdit) {
                    viewModel.updateColor(colorToEdit!!, newName, newRgb)
                } else {
                    viewModel.addColor(
                        name = newName!!,
                        red = newRgb!![0],
                        green = newRgb[1],
                        blue = newRgb[2]
                    )
                }
                showColorDialog = false
                colorToEdit = null
            },
            onDelete = {
                colorToEdit?.let {
                    viewModel.deleteColor(it)
                }
                showColorDialog = false
                colorToEdit = null
            },
            invalidNames = state.colorList.map { it.name },
            isEdit = colorToEdit != null,
            colorToEdit = colorToEdit
        )
    }

    var showDeleteUnusedDialog by remember { mutableStateOf(false) }

    if (showDeleteUnusedDialog) {
        CustomDialog(
            onDismissRequest = {
                showDeleteUnusedDialog = false
            },
            title = {
                Text(
                    text = stringResource(Res.string.delete_unused_colors),
                    style = MaterialTheme.typography.titleLarge,
                )
            },
            leftIcon = {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Close",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.clickable {
                        showDeleteUnusedDialog = false
                    }
                )
            },
            rightIcon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.clickable {
                        viewModel.deleteUnusedColors()
                        showDeleteUnusedDialog = false
                    }
                )
            }
        ) {
            Text(
                text = stringResource(Res.string.delete_unused_colors_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(top = 16.dp)
            )
        }
    }

    Scaffold (
        topBar = {
            CustomTopBar(
                title = { Text(
                    text = stringResource(Res.string.manage_colors),
                    modifier = Modifier
                        .combinedClickable (
                            onClick = {
                                showDeleteUnusedDialog = true
                            },
                            onLongClick = {
                                viewModel.loadDefaultColors()
                            }
                        )
                ) },
                actions = {
                    IconButton(
                        onClick = {
                            showColorDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Add Color",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                openDrawer = openDrawer
            )
        }
    ) { pad ->
        Box(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize()
        ) {
            // TODO: Search bar

            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(state.colorList) { color ->
                    ColorItem(
                        color = color,
                        count = state.colorUses[color.id] ?: 0,
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                colorToEdit = color
                                showColorDialog = true
                            }
                    )
                }
            }
        }
    }
}

