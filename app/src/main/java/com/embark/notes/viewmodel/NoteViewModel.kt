package com.embark.notes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.embark.notes.R
import com.embark.notes.model.Note
import com.embark.notes.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject internal constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    var notes: List<Note> = listOf()
    var pinnedNotes: List<Note> = listOf()
    var unpinnedNotes: List<Note> = listOf()

    var selectedNote: Note? = null
    var editedNote: Note? = null

    var discardingEmptyNote = false
    var archivingNote = false

    var navLocation = R.id.notes

    private val mGetAllNotesLiveData: MutableLiveData<List<Note>> = MutableLiveData<List<Note>>()
    val getAllNotesLiveData: LiveData<List<Note>> = mGetAllNotesLiveData

    private val mGetAllPinnedNotesLiveData: MutableLiveData<List<Note>> =
        MutableLiveData<List<Note>>()
    val getAllPinnedNotesLiveData: LiveData<List<Note>> = mGetAllPinnedNotesLiveData

    private val mGetAllUnpinnedNotesLiveData: MutableLiveData<List<Note>> =
        MutableLiveData<List<Note>>()
    val getAllUnpinnedNotesLiveData: LiveData<List<Note>> = mGetAllUnpinnedNotesLiveData

    fun insert(note: Note) {
        viewModelScope.launch {
            noteRepository.insertAll(note)
        }
    }

    fun getAllNotes() {
        viewModelScope.launch {
            notes = noteRepository.getAllNotes()
            mGetAllNotesLiveData.postValue(notes)

            pinnedNotes = notes.filter { it.isPinned == true }
            unpinnedNotes = notes.filter { it.isPinned == false }
        }
    }

    fun getArchivedNotes() {
        viewModelScope.launch {
            notes = noteRepository.getAllArchivedNotes()
            mGetAllNotesLiveData.postValue(notes)

            pinnedNotes = notes.filter { it.isPinned == true }
            unpinnedNotes = notes.filter { it.isPinned == false }
        }
    }

    fun delete(note: Note) {
        viewModelScope.launch {
            noteRepository.delete(note)
        }
    }

    fun update(note: Note) {
        viewModelScope.launch {
            noteRepository.update(note)
        }
    }

    companion object {
        const val DISPLAY_TYPE_STAGGERED_GRID = 0
        const val DISPLAY_TYPE_LINEAR = 1
    }

}