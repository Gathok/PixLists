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
import de.malteans.pixlists.util.PixDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DefaultPixRepository(
    private val dao: PixDao,
) : PixRepository {

    // List Operations --------------------------------------------------
    override suspend fun createList(name: String): Long {
        val entity = PixListEntity(name = name)
        // upsertList returns the new id
        return dao.upsertList(entity)
    }

    override suspend fun deleteListById(listId: Long) {
        val listEntity = dao.getList(listId).first()
        dao.deleteList(listEntity)
    }

    override suspend fun renameList(listId: Long, newName: String) {
        val listEntity = dao.getList(listId).first()
        val updated = listEntity.copy(name = newName)
        dao.upsertList(updated)
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
                PixDate.fromString(entryEntity.date) to mappedCategories[entryEntity.categoryId]
            }
            listEntity.toPixList(mappedCategories.values.toList(), mappedEntries)
        }
    }

    // Category Operations ----------------------------------------------
    override suspend fun createCategory(listId: Long, colorId: Long, name: String): Long {
        // Determine orderIndex based on current number of categories
        val currentCategories = dao.getCategoriesForList(listId).first()
        val orderIndex = currentCategories.size
        val entity = PixCategoryEntity(
            listId = listId,
            colorId = colorId,
            name = name,
            orderIndex = orderIndex
        )
        return dao.upsertCategory(entity)
    }

    override suspend fun deleteCategory(categoryId: Long) {
        val categoryEntity = dao.getCategory(categoryId).first()
        dao.deleteCategory(categoryEntity)
    }

    override suspend fun renameCategory(categoryId: Long, newName: String) {
        val categoryEntity = dao.getCategory(categoryId).first()
        val updated = categoryEntity.copy(name = newName)
        dao.upsertCategory(updated)
    }

    override suspend fun changeCategoryColor(categoryId: Long, newColorId: Long) {
        val categoryEntity = dao.getCategory(categoryId).first()
        val updated = categoryEntity.copy(colorId = newColorId)
        dao.upsertCategory(updated)
    }

    override suspend fun changeCategoriesOrder(listId: Long, newOrderByIds: List<Long>) {
        newOrderByIds.forEachIndexed { index, catId ->
            val categoryEntity = dao.getCategory(catId).first()
            val updated = categoryEntity.copy(orderIndex = index)
            dao.upsertCategory(updated)
        }
    }

    // Color Operations --------------------------------------------------
    override suspend fun createColor(name: String, red: Float, green: Float, blue: Float): Long {
        val entity = PixColorEntity(name = name, red = red, green = green, blue = blue)
        return dao.upsertColor(entity)
    }

    override suspend fun deleteColor(colorId: Long) {
        // Suggestion: Consider adding a getColor(colorId: Int) function to the DAO.
        val colorEntity = dao.getAllColors().first().firstOrNull { it.id == colorId }
            ?: throw IllegalArgumentException("Color with id $colorId not found")
        dao.deleteColor(colorEntity)
    }

    override suspend fun renameColor(colorId: Long, newName: String) {
        val colorEntity = dao.getAllColors().first().firstOrNull { it.id == colorId }
            ?: throw IllegalArgumentException("Color with id $colorId not found")
        val updated = colorEntity.copy(name = newName)
        dao.upsertColor(updated)
    }

    override suspend fun changeColor(colorId: Long, newRed: Float, newGreen: Float, newBlue: Float) {
        val colorEntity = dao.getAllColors().first().firstOrNull { it.id == colorId }
            ?: throw IllegalArgumentException("Color with id $colorId not found")
        val updated = colorEntity.copy(red = newRed, green = newGreen, blue = newBlue)
        dao.upsertColor(updated)
    }

    override fun getAllColors(): Flow<List<PixColor>> {
        return dao.getAllColors().map { list ->
            list.map { it.toPixColor() }
        }
    }

    // Entry Operations --------------------------------------------------
    override suspend fun createEntry(listId: Long, categoryId: Long, date: PixDate): Long {
        val entity = PixEntryEntity(
            listId = listId,
            date = date.toString(),  // Using the PixDate.toString() format ("MM-dd")
            categoryId = categoryId
        )
        return dao.upsertEntry(entity)
    }

    override suspend fun deleteEntry(listId: Long, date: PixDate) {
        val entries = dao.getEntriesForList(listId).first()
        val entry = entries.firstOrNull { it.date == date.toString() }
            ?: throw IllegalArgumentException("Entry for date $date not found in list $listId")
        dao.deleteEntry(entry)
    }
}
