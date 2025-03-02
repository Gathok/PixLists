package de.malteans.pixlists.domain

import de.malteans.pixlists.util.PixDate

data class PixList(
    val id: Long,
    val name: String,
    val categories: List<PixCategory>,
    val entries: Map<PixDate, PixCategory?>,
)
