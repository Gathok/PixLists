package de.malteans.pixlists.presentation.main

import de.malteans.pixlists.domain.PixList
import de.malteans.pixlists.util.Screen

data class MainState(
    val allPixLists: List<PixList> = emptyList(),
    val curPixListId: Long? = null,
    val curScreen: Screen = Screen.LIST,
)
