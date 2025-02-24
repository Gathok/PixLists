package de.malteans.pixlists.list.data.database

import androidx.room.RoomDatabase

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<PixDatabase>
}