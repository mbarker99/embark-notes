package com.embark.notes.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.embark.notes.model.Note

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}