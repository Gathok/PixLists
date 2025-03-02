package de.malteans.pixlists.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import de.malteans.pixlists.data.database.entities.PixCategoryEntity
import de.malteans.pixlists.data.database.entities.PixColorEntity
import de.malteans.pixlists.data.database.entities.PixEntryEntity
import de.malteans.pixlists.data.database.entities.PixListEntity

@Database(
    entities = [PixListEntity::class, PixEntryEntity::class, PixCategoryEntity::class, PixColorEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PixDatabase : RoomDatabase() {

    abstract val pixDao: PixDao

    companion object {
        const val DB_NAME = "pix.db"
    }
}