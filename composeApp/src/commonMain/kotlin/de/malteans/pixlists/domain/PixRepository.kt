package de.malteans.pixlists.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface PixRepository {

    // List Operations --------------------------------------------------
    suspend fun createList(name: String): Long

    suspend fun deleteListById(listId: Long)

    suspend fun renameList(listId: Long, newName: String)

    fun getAllPixLists(): Flow<List<PixList>>

    fun getCurrentPixList(listId: Long): Flow<PixList?>

    // Category Operations ----------------------------------------------
    suspend fun createCategory(listId: Long, colorId: Long, name: String): Long

    suspend fun deleteCategoryById(categoryId: Long)

    suspend fun renameCategory(categoryId: Long, newName: String)

    suspend fun changeCategoryColor(categoryId: Long, newColorId: Long)

    suspend fun changeCategoriesOrder(listId: Long, newOrderByIds: List<Long>)

    // Color Operations --------------------------------------------------
    suspend fun createColor(name: String, red: Float, green: Float, blue: Float): Long

    suspend fun deleteColor(colorId: Long)

    suspend fun renameColor(colorId: Long, newName: String)

    suspend fun changeColor(colorId: Long, newRed: Float, newGreen: Float, newBlue: Float)

    fun getAllColors(): Flow<List<PixColor>>

    // Entry Operations --------------------------------------------------
    suspend fun createEntry(listId: Long, categoryId: Long, date: LocalDate): Long

    suspend fun setEntry(listId: Long, categoryId: Long, date: LocalDate): Long

    suspend fun deleteEntry(listId: Long, date: LocalDate)
}
