package de.malteans.pixlists.list.domain

data class PixCategory(
    val id: Long,
    val listId: Long,
    val colorId: Long?,
    val name: String,
    val orderIndex: Int,
)
