package de.malteans.pixlists.presentation.main

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import de.malteans.pixlists.app.Route
import de.malteans.pixlists.domain.PixList
import de.malteans.pixlists.presentation.main.util.NavGraph
import de.malteans.pixlists.presentation.main.util.NavListHeader
import de.malteans.pixlists.presentation.util.customDialogs.CustomDialog
import de.malteans.pixlists.presentation.util.customDialogs.NewListDialog
import de.malteans.pixlists.presentation.util.customIcons.AddPixListIcon
import de.malteans.pixlists.presentation.util.customIcons.FilledManageColor
import de.malteans.pixlists.presentation.util.customIcons.FilledPixListIcon
import de.malteans.pixlists.presentation.util.customIcons.OutlinedManageColor
import de.malteans.pixlists.presentation.util.customIcons.OutlinedPixListIcon
import de.malteans.pixlists.util.Screen
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()
    val state by viewModel.state.collectAsState()
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)

    val selectedScreen = remember { mutableStateOf(Screen.LIST) }
    val selectedPixListId = remember { mutableStateOf<Long?>(null) }

    var showNewListDialog by remember { mutableStateOf(false) }

    var showDeleteListDialog by remember { mutableStateOf(false) }
    var listToDelete by remember { mutableStateOf<PixList?>(null) }

    if (showNewListDialog) {
        NewListDialog(
            onDismiss = { showNewListDialog = false },
            onAdd = { name ->
                val newId = viewModel.createPixList(name.trim())
                showNewListDialog = false
                selectedScreen.value = Screen.LIST
                selectedPixListId.value = newId
                navController.navigate(Route.LoadingScreen)
                scope.launch {
                    drawerState.close()
                    navController.navigate(Route.ListScreen(newId))
                }
            },
            invalidNames = state.allPixLists.map { it.name },
        )
    }

    if (showDeleteListDialog && listToDelete != null) {
        CustomDialog(
            onDismissRequest = { showDeleteListDialog = false },
            title = {
                Text(
                    text = "Delete PixList",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            leftIcon = {
                IconButton (
                    onClick = {
                        showDeleteListDialog = false
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            rightIcon = {
                TextButton(
                    onClick = {
                        viewModel.deletePixListById(listToDelete!!.id)
                        showDeleteListDialog = false
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Yes",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            },
        ) {
            Text(
                text = "Sure you want to delete ${listToDelete!!.name}?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(top = 16.dp)
            )
        }
    }

    var showHiddenLists by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Spacer(modifier = Modifier.height(8.dp))
                    NavListHeader(onLongClick = {
                        showHiddenLists = !showHiddenLists
                        /*
                        Toast.makeText(
                        context,
                        if (showHiddenLists) SHOW_HIDDEN_LISTS else HIDE_HIDDEN_LISTS,
                        Toast.LENGTH_SHORT
                        ).show()
                        // Vibrate
                        (getSystemService(context, Vibrator::class.java) as Vibrator)
                        .vibrate(
                        VibrationEffect
                        .createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
                        )
                        */
                    })
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyColumn (
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        items(state.allPixLists) { curPixList ->
                            if (!(curPixList.name.matches(Regex("^\\(.*\\)$")) && !showHiddenLists)) {
                                NavigationDrawerItem(
                                    label = { Text(curPixList.name) },
                                    selected = curPixList.id == selectedPixListId.value,
                                    onClick = {
                                        selectedPixListId.value = curPixList.id
                                        selectedScreen.value = Screen.LIST
                                        scope.launch {
                                            navController.navigate(Route.LoadingScreen)
                                            drawerState.close()
                                            navController.navigate(Route.ListScreen(curPixList.id))
                                        }
                                    },
                                    modifier = Modifier
                                        .padding(NavigationDrawerItemDefaults.ItemPadding),
                                    icon = {
                                        Icon(
                                            imageVector = if (curPixList.id == selectedPixListId.value) {
                                                FilledPixListIcon
                                            } else {
                                                OutlinedPixListIcon
                                            },
                                            contentDescription = "PixList"
                                        )
                                    },
                                    badge = {
                                        IconButton(
                                            onClick = {
                                                listToDelete = curPixList
                                                selectedPixListId.value = null
                                                selectedScreen.value = Screen.LIST
                                                showDeleteListDialog = true
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete PixList"
                                            )
                                        }
                                    }
                                )
                            }
                        }
                        item {
                            NavigationDrawerItem(
                                label = { Text("New PixList") },
                                onClick = {
                                    showNewListDialog = true
                                    selectedPixListId.value = null
                                },
                                selected = false,
                                modifier = Modifier
                                    .padding(NavigationDrawerItemDefaults.ItemPadding),
                                icon = {
                                    Icon(
                                        imageVector = AddPixListIcon,
                                        contentDescription = "Add PixList"
                                    )
                                }
                            )
                        }
                    }
                    NavigationDrawerItem(
                        label = { Text("Manage colors") },
                        onClick = {
                            navController.navigate(Route.ManageColorsScreen)
                            selectedPixListId.value = null
                            selectedScreen.value = Screen.MANAGE_COLORS
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        selected = selectedScreen.value == Screen.MANAGE_COLORS,
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding),
                        icon = {
                            if (selectedScreen.value == Screen.MANAGE_COLORS) {
                                Icon(
                                    imageVector = FilledManageColor,
                                    contentDescription = "Manage Colors"
                                )
                            } else {
                                Icon(
                                    imageVector = OutlinedManageColor,
                                    contentDescription = "Manage Colors"
                                )
                            }
                        }
                    )
                }
            }
        ) {
            NavGraph(
                navController = navController, openDrawer = { scope.launch { drawerState.open() } }
            )
        }
    }
}