package com.embark.notes.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.embark.notes.model.Note
import com.embark.notes.util.Constants

@Dao
interface NoteDao {
    @Query("SELECT * FROM ${Constants.DB_NAME} ORDER BY ${Constants.DB_LAST_MODIFIED} DESC")
    suspend fun getAllNotes(): List<Note>

    @Query("SELECT * FROM ${Constants.DB_NAME} WHERE ${Constants.DB_TITLE} LIKE :${Constants.DB_TITLE} LIMIT 1")
    suspend fun getAllNotesByName(title: String): List<Note>

    @Query("SELECT * FROM ${Constants.DB_NAME} WHERE ${Constants.DB_IS_PINNED} = 1 ORDER BY ${Constants.DB_LAST_MODIFIED} DESC")
    suspend fun getAllPinnedNotes(): List<Note>

    @Query("SELECT * FROM ${Constants.DB_NAME} WHERE ${Constants.DB_IS_PINNED} = 0 ORDER BY ${Constants.DB_LAST_MODIFIED} DESC")
    suspend fun getAllUnpinnedNotes(): List<Note>

    @Insert
    suspend fun insertAll(vararg notes: Note)

    @Delete
    suspend fun delete(note: Note)

    @Update
    suspend fun update(note: Note)
}