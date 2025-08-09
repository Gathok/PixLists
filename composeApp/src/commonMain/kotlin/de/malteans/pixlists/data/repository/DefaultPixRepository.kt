package de.malteans.pixlists.data.repository

import de.malteans.pixlists.data.database.PixDao
import de.malteans.pixlists.data.database.entities.PixCategoryEntity
import de.malteans.pixlists.data.database.entities.PixColorEntity
import de.malteans.pixlists.data.database.entities.PixEntryEntity
import de.malteans.pixlists.data.database.entities.PixListEntity
import de.malteans.pixlists.data.mappers.toPixCategory
import de.malteans.pixlists.data.mappers.toPixColor
import de.malteans.pixlists.data.mappers.toPixList
import de.malteans.pixlists.domain.PixColor
import de.malteans.pixlists.domain.PixList
import de.malteans.pixlists.domain.PixRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

class DefaultPixRepository(
    private val dao: PixDao,
) : PixRepository {

    // List Operations ----------------------------------------------------------------------------
    override suspend fun createList(name: String): Long {
        return dao.upsertList(PixListEntity(name = name))
    }

    override suspend fun deleteListById(listId: Long) {
        dao.deleteListById(listId)
    }

    override suspend fun renameList(listId: Long, newName: String) {
        dao.renameList(listId, newName)
    }

    override fun getAllPixLists(): Flow<List<PixList>> {
        return dao.getAllLists().map { allLists ->
            allLists.map { listEntity ->
                listEntity.toPixList()
            }
        }
    }

    override fun getCurrentPixList(listId: Long): Flow<PixList> {
        return combine(
            dao.getList(listId),
            dao.getAllColors(),
            dao.getCategoriesForList(listId),
            dao.getEntriesForList(listId)
        ) { listEntity, colors, categories, entries ->
            val colorsMap = colors.associate { it.id to it.toPixColor() }
            val mappedCategories = categories.associate { categoryEntity ->
                categoryEntity.id to categoryEntity.toPixCategory(colorsMap[categoryEntity.colorId])
            }
            val mappedEntries = entries.associate { entryEntity ->
                entryEntity.date to mappedCategories[entryEntity.categoryId]
            }
            listEntity.toPixList(mappedCategories.values.toList(), mappedEntries)
        }
    }

    // Category Operations ----------------------------------------------
    override suspend fun createCategory(listId: Long, colorId: Long, name: String): Long {
        // Determine orderIndex based on current number of categories
        val orderIndex = dao.getCategoryCountForList(listId)
        return dao.upsertCategory(PixCategoryEntity(
            listId = listId,
            colorId = colorId,
            name = name,
            orderIndex = orderIndex
        ))
    }

    override suspend fun deleteCategoryById(categoryId: Long) {
        dao.deleteCategoryById(categoryId)
    }

    override suspend fun renameCategory(categoryId: Long, newName: String) {
        dao.renameCategory(categoryId, newName)
    }

    override suspend fun changeCategoryColor(categoryId: Long, newColorId: Long) {
        dao.changeCategoryColor(categoryId, newColorId)
    }

    override suspend fun changeCategoriesOrder(listId: Long, newOrderByIds: List<Long>) {
        newOrderByIds.forEachIndexed { orderIndex, categoryId ->
            dao.changeCategoryOrderIndex(categoryId, orderIndex)
        }
    }

    // Color Operations --------------------------------------------------
    override suspend fun createColor(name: String, red: Float, green: Float, blue: Float): Long {
        return dao.upsertColor(PixColorEntity(name = name, red = red, green = green, blue = blue))
    }

    override suspend fun deleteColor(colorId: Long) {
        dao.deleteColorById(colorId)
    }

    override suspend fun renameColor(colorId: Long, newName: String) {
        dao.renameColor(colorId, newName)
    }

    override suspend fun changeColor(colorId: Long, newRed: Float, newGreen: Float, newBlue: Float) {
        dao.changeColor(colorId, newRed, newGreen, newBlue)
    }

    override fun getAllColors(): Flow<List<PixColor>> {
        return dao.getAllColors().map { list ->
            list.map { it.toPixColor() }
        }
    }

    // Entry Operations --------------------------------------------------
    override suspend fun createEntry(listId: Long, categoryId: Long, date: LocalDate): Long {
        return dao.upsertEntry(PixEntryEntity(
            listId = listId,
            date = date,
            categoryId = categoryId
        ))
    }

    override suspend fun setEntry(listId: Long, categoryId: Long, date: LocalDate): Long {
        return dao.upsertEntry(PixEntryEntity(
            listId = listId,
            date = date,
            categoryId = categoryId
        ))
    }

    override suspend fun deleteEntry(listId: Long, date: LocalDate) {
        dao.deleteEntryByListIdAndDate(listId, date)
    }
}
