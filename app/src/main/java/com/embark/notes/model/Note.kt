package com.embark.notes.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "index")
    val index: Long = 0L,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "content") val content: String?,
    @ColumnInfo(name = "isPinned") var isPinned: Boolean? = false,
    @ColumnInfo(name = "lastModified") val lastModified: Long
)
