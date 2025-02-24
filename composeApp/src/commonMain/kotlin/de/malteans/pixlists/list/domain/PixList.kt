package de.malteans.pixlists.list.domain

import de.malteans.pixlists.list.util.PixDate

data class PixList(
    val id: Long,
    val name: String,
    val categories: List<PixCategory>,
    val entries: Map<PixDate, PixCategory>,
)
