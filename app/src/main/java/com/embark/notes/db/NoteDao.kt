package com.embark.notes.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.embark.notes.model.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM Note ORDER BY lastModified DESC")
    suspend fun getAllNotes(): List<Note>

    @Query("SELECT * FROM Note WHERE title LIKE :title LIMIT 1")
    suspend fun getAllNotesByName(title: String) : List<Note>

    @Insert
    suspend fun insertAll(vararg notes: Note)

    @Delete
    suspend fun delete(note: Note)

    @Update
    suspend fun update(note: Note)
}