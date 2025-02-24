package de.malteans.pixlists.list.presentation.view

import de.malteans.pixlists.list.presentation.PixColorUiState
import de.malteans.pixlists.list.presentation.PixListUiState

data class ViewListState(
    val curListId: Int? = null,
    val curList: PixListUiState? = null,
    val colors: List<PixColorUiState> = emptyList(),
)
