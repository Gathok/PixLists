package de.malteans.pixlists.presentation.list

import de.malteans.pixlists.domain.PixCategory
import de.malteans.pixlists.domain.PixColor
import de.malteans.pixlists.domain.PixList
import de.malteans.pixlists.presentation.list.components.ListStatus

data class ListState(
    val listStatus: ListStatus = ListStatus.LOADING,
    val curPixList: PixList? = null,
    val curCategories: List<PixCategory> = emptyList(),
    val colorList: List<PixColor> = emptyList(),
    val invalideNames: List<String> = emptyList(),
)
