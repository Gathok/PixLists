package de.malteans.pixlists.presentation.list

import de.malteans.pixlists.domain.PixCategory
import de.malteans.pixlists.domain.PixColor
import de.malteans.pixlists.domain.PixList

data class ListState(
    val isLoading: Boolean = false, // TODO: implement loading in state not local
    val curPixList: PixList? = null,
    val curCategories: List<PixCategory> = emptyList(),
    val colorList: List<PixColor> = emptyList(),
)
