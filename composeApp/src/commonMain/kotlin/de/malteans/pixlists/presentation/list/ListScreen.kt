package de.malteans.pixlists.presentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.malteans.pixlists.domain.PixCategory
import de.malteans.pixlists.presentation.components.CustomTopBar
import de.malteans.pixlists.presentation.components.SnackbarManager
import de.malteans.pixlists.presentation.components.customIcons.FilledPixIcon
import de.malteans.pixlists.presentation.components.customIcons.OutlinedPixIcon
import de.malteans.pixlists.presentation.list.components.CategoryDialog
import de.malteans.pixlists.presentation.list.components.EntryDialog
import de.malteans.pixlists.presentation.list.components.ListStatus
import de.malteans.pixlists.presentation.list.components.RenamePixListDialog
import de.malteans.pixlists.domain.Months
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
fun ListScreenRoot(
    viewModel: ListViewModel = koinViewModel(),
    openDrawer: () -> Unit,
    curPixListId: Long?,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(curPixListId) {
        when (curPixListId) {
            null -> viewModel.onAction(ListAction.SetListStatus(ListStatus.EMPTY))
            else -> {
                viewModel.onAction(ListAction.SetListStatus(ListStatus.LOADING))
                viewModel.onAction(ListAction.SetPixListId(curPixListId))
            }
        }
    }

    LaunchedEffect(state.curPixList) {
        if (state.curPixList != null) {
            viewModel.onAction(ListAction.SetListStatus(ListStatus.OPENED))
        }
    }

    ListScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is ListAction.OpenDrawer-> openDrawer()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@OptIn(ExperimentalTime::class)
@Composable
fun ListScreen(
    state: ListState,
    onAction: (ListAction) -> Unit,
) {
    val scope = rememberCoroutineScope()

//    BackHandler {
//        viewModel.undoLastAction()
//    }

    var showEntryDialog by remember { mutableStateOf(false) }
    var entryToEdit by remember { mutableStateOf<LocalDate?>(null) }
    var curEntryCategory by remember { mutableStateOf<PixCategory?>(null) }

    if (showEntryDialog) {
        val startDate: LocalDate? = entryToEdit
        val curCategory: PixCategory? = curEntryCategory
        if (startDate != null) {
            entryToEdit = null
        }
        if (curCategory != null) {
            curEntryCategory = null
        }
        EntryDialog(
            categories = state.curCategories,
            onDismiss = { showEntryDialog = false },
            onEdit = { date, category ->
                onAction(ListAction.SetPixEntry(date, category))
                showEntryDialog = false
            },
            startDate = startDate
                ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
            curCategory = curCategory
        )
    }

    var showCategoryDialog by remember { mutableStateOf(false) }
    var categoryToEdit by remember { mutableStateOf<PixCategory?>(null) }

    if (showCategoryDialog) {
        CategoryDialog(
            onDismiss = {
                showCategoryDialog = false
                categoryToEdit = null
            },
            onAdd = { name, color, isEdit ->
                if (isEdit) {
                    onAction(ListAction.UpdatePixCategory(
                        categoryToEdit!!,
                        name,
                        color
                    ))
                } else {
                    onAction(ListAction.CreatePixCategory(
                        name!!,
                        color!!
                    ))
                }
                showCategoryDialog = false
                categoryToEdit = null
            },
            onDelete = {
                onAction(ListAction.DeletePixCategory(categoryToEdit!!))
                showCategoryDialog = false
                categoryToEdit = null
            },
            colors = state.colorList,
            invalidNames = state.curCategories.map { it.name },
            isEdit = categoryToEdit != null,
            categoryToEdit = categoryToEdit,
        )
    }

    var showRenameDialog by remember { mutableStateOf(false) }

    if (showRenameDialog) {
        RenamePixListDialog(
            curName = state.curPixList?.name ?: "",
            invalideNames = state.invalideNames,
            onDismiss = { showRenameDialog = false },
            onFinish = { newName ->
                onAction(ListAction.UpdatePixListName(newName))
                showRenameDialog = false
            }
        )
    }

    Scaffold (
        topBar = {
            CustomTopBar(
                title = {
                    if (state.curPixList != null) {
                        Text(
                            text = state.curPixList.name,
                            modifier = Modifier
                                .clickable { showRenameDialog = true }
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (state.curPixList != null  && state.curCategories.isNotEmpty()) {
                                showEntryDialog = true
                            } else {
                                scope.launch {
                                    SnackbarManager.showSnackbar(
                                        message = "Create a PixList and at least one Category first.",
                                        duration = SnackbarDuration.Short,
                                        withDismissAction = true
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Add Entry",
                            tint = if (state.curCategories.isNotEmpty() && state.listStatus == ListStatus.OPENED) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            }
                        )
                    }
                },
                openDrawer = { onAction(ListAction.OpenDrawer) },
            )
        }
    ) { pad ->
        Box (
            modifier = Modifier
                .padding(pad)
                .padding(start = 8.dp, end = 4.dp, bottom = 16.dp)
        ) {
            when (state.listStatus) {
                ListStatus.LOADING -> {
                    Column (
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Loading...")
                    }
                }
                ListStatus.OPENED -> {
                    Row {
                        Column (
                            modifier = Modifier.weight(0.8f)
                        ) {
                            Row (
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                            ) {
                                for (monthNumber in 0..12) {
                                    Column (
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier
                                            .weight(1f / 13f)
                                    ) {
                                        when (monthNumber) {
                                            0 -> {
                                                for (j in 0..31) {
                                                    Row (
                                                        modifier = Modifier
                                                            .weight(1f / 32f),
                                                        horizontalArrangement = Arrangement.End,
                                                        verticalAlignment = Alignment.CenterVertically,
                                                    ) {
                                                        val text = if (j == 0) {
                                                            null
                                                        } else if (j < 10) {
                                                            "0$j"
                                                        } else {
                                                            "$j"
                                                        }
                                                        if (text != null) {
                                                            Text (
                                                                text = text,
                                                                style = MaterialTheme.typography.labelMedium,
                                                                color = MaterialTheme.colorScheme.onSurface,
                                                                textAlign = TextAlign.Center,
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                            else -> {
                                                val month = Months.getByIndex(monthNumber)
                                                for (day in 0..month.getDaysCount) {
                                                    Row (
                                                        modifier = Modifier
                                                            .weight(1f / 32f),
                                                        verticalAlignment = Alignment.Bottom,
                                                    ) {
                                                        if (day == 0) {
                                                            Text(
                                                                text = month.getShortStringId,
                                                                style = MaterialTheme.typography.labelMedium,
                                                                color = MaterialTheme.colorScheme.onSurface,
                                                            )
                                                        } else {
                                                            val currentDate = LocalDate(2025, monthNumber, day)
                                                            state.curPixList!!.entries[currentDate]
                                                                .let { pixCategory ->
                                                                    IconButton(
                                                                        onClick = {
                                                                            if (state.curCategories.isNotEmpty()) {
                                                                                entryToEdit = currentDate
                                                                                curEntryCategory = pixCategory
                                                                                showEntryDialog = true
                                                                            } else {
                                                                                //                                                                    Toast.makeText(
                                                                                //                                                                        context,
                                                                                //                                                                        context.getString(R.string.create_category_first_desc),
                                                                                //                                                                        Toast.LENGTH_SHORT
                                                                                //                                                                    ).show()
                                                                            }
                                                                        }
                                                                    ) {
                                                                        if (pixCategory == null) {
                                                                            Icon(
                                                                                imageVector = OutlinedPixIcon,
                                                                                contentDescription = "Empty Pix",
                                                                                tint = MaterialTheme.colorScheme.onSurface.copy(
                                                                                    alpha = 0.5f
                                                                                ),
                                                                            )
                                                                        } else {
                                                                            if (pixCategory.color != null) {
                                                                                Icon(
                                                                                    imageVector = FilledPixIcon,
                                                                                    contentDescription = "Pix",
                                                                                    tint = pixCategory.color.toColor(),
                                                                                )
                                                                            } else {
                                                                                Icon(
                                                                                    imageVector = OutlinedPixIcon,
                                                                                    contentDescription = "Empty Pix",
                                                                                    tint = MaterialTheme.colorScheme.error
                                                                                )
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                        }
                                                    }
                                                }
                                                for (r in 0 until 31 - month.getDaysCount) {
                                                    Row (
                                                        modifier = Modifier
                                                            .weight(1f / 32f),
                                                    ) {
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        // Categories -----------------------------------------------------------------
                        Column(
                            modifier = Modifier
                                .weight(0.2f)
                                .padding(start = 4.dp, top = 8.dp)
                                .fillMaxSize()
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                items(state.curCategories) { category ->
                                    Column (
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(4.dp)
                                            .clickable {
                                                categoryToEdit = category
                                                showCategoryDialog = true
                                            },
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Row {
                                            if (category.color != null) {
                                                Icon(
                                                    imageVector = FilledPixIcon,
                                                    contentDescription = "Pix Category Icon",
                                                    tint = category.color.toColor(),
                                                )
                                            } else {
                                                Icon(
                                                    imageVector = OutlinedPixIcon,
                                                    contentDescription = "Empty Pix",
                                                    tint = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }
                                        Row {
                                            Text(
                                                text = category.name,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                textAlign = TextAlign.Center,
                                            )
                                        }
                                    }
                                }
                                item {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.Top,
                                    ) {
                                        IconButton(
                                            onClick = {
                                                categoryToEdit = null
                                                showCategoryDialog = true
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.AddBox,
                                                contentDescription = "Add Category"
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                ListStatus.EMPTY -> {
                    Column (
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No PixList selected")
                    }
                }
            }
        }
    }
}