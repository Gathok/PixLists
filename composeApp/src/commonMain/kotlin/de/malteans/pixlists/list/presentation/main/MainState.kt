package de.malteans.pixlists.list.presentation.main

import de.malteans.pixlists.list.domain.PixList
import de.malteans.pixlists.list.util.Screen

data class MainState(
    val curScreen: Screen = Screen.LIST,

    val curPixList: PixList? = null,
    val allPixLists: List<PixList> = emptyList(),
)
