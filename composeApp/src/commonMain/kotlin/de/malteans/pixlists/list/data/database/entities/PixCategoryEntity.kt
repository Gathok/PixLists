package de.malteans.pixlists.list.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = PixListEntity::class,
            parentColumns = ["id"],
            childColumns = ["listId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PixColorEntity::class,
            parentColumns = ["id"],
            childColumns = ["colorId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class PixCategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val listId: Long,
    val colorId: Long? = null,
    val name: String,
    val orderIndex: Int,
)
