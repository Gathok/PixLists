package de.malteans.pixlists.list.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory

actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<PixDatabase> {
        val dbFile = NSHomeDirectory() + "/${PixDatabase.DB_NAME}"
        return Room.databaseBuilder<PixDatabase>(
            name = dbFile
        )
    }
}