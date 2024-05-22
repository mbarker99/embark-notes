package com.embark.notes.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.embark.notes.model.Note

@Database(entities = [Note::class], version = 3)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @JvmField
        val MIGRATION_1_2 = Migration1to2()

        @JvmField
        val MIGRATION_2_3 = Migration1to2()
    }

}