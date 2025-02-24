package de.malteans.pixlists.list.data.mappers

import de.malteans.pixlists.list.data.database.entities.PixCategoryEntity
import de.malteans.pixlists.list.domain.PixCategory

fun PixCategoryEntity.toPixCategory() : PixCategory {
    return PixCategory(
        id = id,
        listId = listId,
        colorId = colorId,
        name = name,
        orderIndex = orderIndex,
    )
}

fun PixCategory.toPixCategoryEntity() : PixCategoryEntity {
    return PixCategoryEntity(
        id = id,
        listId = listId,
        colorId = colorId,
        name = name,
        orderIndex = orderIndex,
    )
}