package de.malteans.pixlists.presentation.manageColors

import de.malteans.pixlists.domain.PixCategory
import de.malteans.pixlists.domain.PixColor

data class ManageColorsState(
    val colorList: List<PixColor> = emptyList(),
    val allCategories: List<PixCategory> = emptyList(),
    val colorUses: Map<Long, Int> = emptyMap(),
)
