package de.malteans.pixlists.presentation.main

import de.malteans.pixlists.domain.PixList

data class MainState(
    val curPixList: PixList? = null,
    val allPixLists: List<PixList> = emptyList(),
)
