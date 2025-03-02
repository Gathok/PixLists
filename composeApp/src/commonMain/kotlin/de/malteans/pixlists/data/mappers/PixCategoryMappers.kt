package de.malteans.pixlists.data.mappers

import de.malteans.pixlists.data.database.entities.PixCategoryEntity
import de.malteans.pixlists.domain.PixCategory
import de.malteans.pixlists.domain.PixColor

fun PixCategoryEntity.toPixCategory(color: PixColor?) : PixCategory {
    return PixCategory(
        id = id,
        listId = listId,
        color = color,
        name = name,
        orderIndex = orderIndex,
    )
}