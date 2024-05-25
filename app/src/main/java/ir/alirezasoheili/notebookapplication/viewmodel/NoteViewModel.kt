package ir.alirezasoheili.notebookapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ir.alirezasoheili.notebookapplication.model.Note
import ir.alirezasoheili.notebookapplication.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(
    app: Application, private val repository: NoteRepository
) : AndroidViewModel(app) {
    fun addNote(note: Note) {
        viewModelScope.launch {
            repository.insertNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    fun getAllNotes() = repository.getAllNotes()

    fun searchNote(query: String?) = repository.searchNote(query)


}