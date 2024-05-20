package com.embark.notes.viewmodel

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
    fun insert(note: Note) {
        viewModelScope.launch {
            noteRepository.insertAll(note)
        }
    }
}