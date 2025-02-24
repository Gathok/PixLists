package de.malteans.pixlists.list.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ViewListScreen(
    viewModel: ViewListViewModel,
    curListId: Int? = null,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()


}