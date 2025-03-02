package de.malteans.pixlists.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

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
    }
}
