package de.malteans.pixlists.list.presentation

data class PixCategoryUiState(
    var id: Int,
    val name: String,
    val orderIndex: Int,
    val color: PixColorUiState,
)
