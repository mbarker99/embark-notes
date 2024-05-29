package com.embark.notes.repository

import com.embark.notes.db.NoteDatabase
import com.embark.notes.model.Note
import javax.inject.Inject

class NoteRepository @Inject constructor(private val db: NoteDatabase) {
    suspend fun getAllNotes(): List<Note> = db.noteDao().getAllNotes()

    suspend fun getAllNotesByName(title: String): List<Note> = db.noteDao().getAllNotesByName(title)

    suspend fun insertAll(vararg notes: Note) = db.noteDao().insertAll(*notes)

    suspend fun delete(note: Note) = db.noteDao().delete(note)

    suspend fun update(note: Note) = db.noteDao().update(note)
}