package de.malteans.pixlists.list.presentation

import de.malteans.pixlists.list.util.PixDate

data class PixListUiState(
    val id: Int,
    val name: String,
    val categories: List<PixCategoryUiState>,
    val entries: Map<PixDate, List<PixCategoryUiState>>,
)
