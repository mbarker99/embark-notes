package com.embark.notes.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    val title: String?,
    val content: String?,
    val lastModified: Long,
    val createdAt: Long,
    var label: String? = null,
    @ColumnInfo(defaultValue = "0") var isPinned: Boolean = false,
    @ColumnInfo(defaultValue = "0") var isArchived: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "index")
    val index: Long = 0L
)
