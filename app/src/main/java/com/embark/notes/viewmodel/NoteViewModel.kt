package com.embark.notes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.embark.notes.model.Note
import com.embark.notes.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject internal constructor(
    private val noteRepository: NoteRepository
): ViewModel() {

    var notes: List<Note> = listOf()

    private val mGetAllNotesLiveData: MutableLiveData<List<Note>> = MutableLiveData<List<Note>>()
    val getAllNotesLiveData: LiveData<List<Note>> = mGetAllNotesLiveData

    fun insert(note: Note) {
        viewModelScope.launch {
            noteRepository.insertAll(note)
        }
    }

    fun getAllNotes() {
        viewModelScope.launch {
            notes = noteRepository.getAllNotes()
            mGetAllNotesLiveData.postValue(notes)
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
}