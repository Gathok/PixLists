package de.malteans.pixlists.list.presentation.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.DrawerValue
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.malteans.pixlists.list.presentation.main.components.NewListDialog
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreenRoot(
    viewModel: MainViewModel = koinViewModel(),
    onListSelected: (Int) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MainScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is MainAction.SelectList -> onListSelected(action.listId)
                else -> Unit
            }
            viewModel.onAction(action)
        },
    )
}

@Composable
fun MainScreen(
    state: MainState,
    onAction: (MainAction) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Open)

    var showNewListDialog by remember { mutableStateOf(false) }

    if (showNewListDialog) {
        NewListDialog(
            onDismiss = { showNewListDialog = false },
            onConfirm = { name ->
                onAction(MainAction.CreateList(name))
                showNewListDialog = false
            },
            invalidNames = state.allPixLists.map { it.name },
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        ModalDrawer(
            drawerState = drawerState,
            drawerContent = {
                Text("PixLists")
            }
        ) {
            Text("Hello, PixLists!")
        }
    }
}