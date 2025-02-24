package de.malteans.pixlists.list.data.database

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object PixDatabaseConstructor: RoomDatabaseConstructor<PixDatabase> {
    override fun initialize(): PixDatabase
}