package de.malteans.pixlists.list.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import de.malteans.pixlists.list.data.database.entities.PixCategoryEntity
import de.malteans.pixlists.list.data.database.entities.PixColorEntity
import de.malteans.pixlists.list.data.database.entities.PixEntryEntity
import de.malteans.pixlists.list.data.database.entities.PixListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PixDao {

    // PixList Operations ------------------------------------------------------
    @Upsert
    suspend fun upsertList(list: PixListEntity): Long

    @Delete
    suspend fun deleteList(list: PixListEntity)

    @Query("SELECT * FROM PixListEntity WHERE id = :listId")
    fun getList(listId: Long): Flow<PixListEntity>

    @Query("SELECT * FROM PixListEntity")
    fun getAllLists(): Flow<List<PixListEntity>>

    // PixEntry Operations -----------------------------------------------------
    @Upsert
    suspend fun upsertEntry(entry: PixEntryEntity): Long

    @Delete
    suspend fun deleteEntry(entry: PixEntryEntity)

    @Query("SELECT * FROM PixEntryEntity WHERE listId = :listId AND id = :entryId")
    fun getEntry(listId: Long, entryId: Long): Flow<PixEntryEntity>

    @Query("SELECT * FROM PixEntryEntity WHERE listId = :listId")
    fun getEntriesForList(listId: Long): Flow<List<PixEntryEntity>>

    // PixCategory Operations --------------------------------------------------
    @Upsert
    suspend fun upsertCategory(category: PixCategoryEntity): Long

    @Delete
    suspend fun deleteCategory(category: PixCategoryEntity)

    @Query("SELECT * FROM PixCategoryEntity WHERE id = :categoryId")
    fun getCategory(categoryId: Long): Flow<PixCategoryEntity>

    @Query("SELECT * FROM PixCategoryEntity WHERE listId = :listId")
    fun getCategoriesForList(listId: Long): Flow<List<PixCategoryEntity>>

    // PixColor Operations -----------------------------------------------------
    @Upsert
    suspend fun upsertColor(color: PixColorEntity): Long

    @Delete
    suspend fun deleteColor(color: PixColorEntity)

    @Query("SELECT * FROM PixColorEntity WHERE id = :colorId")
    fun getColor(colorId: Long): Flow<PixColorEntity>

    @Query("SELECT * FROM PixColorEntity")
    fun getAllColors(): Flow<List<PixColorEntity>>
}
