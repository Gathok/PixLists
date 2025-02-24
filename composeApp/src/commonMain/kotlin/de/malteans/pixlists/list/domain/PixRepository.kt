package de.malteans.pixlists.list.domain

import de.malteans.pixlists.list.util.PixDate
import kotlinx.coroutines.flow.Flow

interface PixRepository {

    // List Operations --------------------------------------------------
    suspend fun createList(name: String): Long

    suspend fun deleteList(listId: Long)

    suspend fun renameList(listId: Long, newName: String)

    suspend fun getAllLists(): Flow<List<PixList>>

    suspend fun getFullListById(listId: Long): PixList

    // Category Operations ----------------------------------------------
    suspend fun createCategory(listId: Long, colorId: Long, name: String): Long

    suspend fun deleteCategory(categoryId: Long)

    suspend fun renameCategory(categoryId: Long, newName: String)

    suspend fun changeCategoryColor(categoryId: Long, newColorId: Long)

    suspend fun getCategoriesForList(listId: Long): Flow<List<PixCategory>>

    suspend fun changeCategoriesOrder(listId: Long, newOrderByIds: List<Long>)

    // Color Operations --------------------------------------------------
    suspend fun createColor(name: String, red: Float, green: Float, blue: Float): Long

    suspend fun deleteColor(colorId: Long)

    suspend fun renameColor(colorId: Long, newName: String)

    suspend fun changeColor(colorId: Long, newRed: Float, newGreen: Float, newBlue: Float)

    suspend fun getAllColors(): Flow<List<PixColor>>

    // Entry Operations --------------------------------------------------
    suspend fun createEntry(listId: Long, categoryId: Long, date: PixDate): Long

    suspend fun deleteEntry(listId: Long, date: PixDate)

    suspend fun changeEntryCategory(listId: Long, date: PixDate, newCategoryId: Long)
}
