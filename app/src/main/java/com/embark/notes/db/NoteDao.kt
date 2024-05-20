package com.embark.notes.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.embark.notes.model.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM Note")
    suspend fun getAllNotes(): List<Note>

    @Query("SELECT * FROM Note WHERE title LIKE :title LIMIT 1")
    suspend fun getAllNotesByName(title: String) : List<Note>

    @Insert
    suspend fun insertAll(vararg notes: Note)

    @Delete
    suspend fun delete(note: Note)
}