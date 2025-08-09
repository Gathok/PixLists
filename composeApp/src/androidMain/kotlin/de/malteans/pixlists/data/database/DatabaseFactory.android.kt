package de.malteans.pixlists.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import de.malteans.pixlists.data.database.migration.MIGRATION1_2

actual class DatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<PixDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(PixDatabase.DB_NAME)
        return Room.databaseBuilder<PixDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        )
            .addMigrations(
                PixDatabase.Companion.MIGRATION1_2,
            )
    }
}
