package de.malteans.pixlists.list.data.mappers

import de.malteans.pixlists.list.data.database.entities.PixListEntity
import de.malteans.pixlists.list.domain.PixCategory
import de.malteans.pixlists.list.domain.PixList
import de.malteans.pixlists.list.util.PixDate

fun PixListEntity.toPixList(
    categories: List<PixCategory> = emptyList(),
    entries: Map<PixDate, PixCategory> = emptyMap()
): PixList {
    return PixList(
        id = id,
        name = name,
        categories = categories,
        entries = entries,
    )
}

fun PixList.toPixListEntity(): PixListEntity {
    return PixListEntity(
        id = id,
        name = name,
    )
}