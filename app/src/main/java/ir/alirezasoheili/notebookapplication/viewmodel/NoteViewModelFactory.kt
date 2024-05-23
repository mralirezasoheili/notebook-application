package ir.alirezasoheili.notebookapplication.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ir.alirezasoheili.notebookapplication.repository.NoteRepository

class NoteViewModelFactory(
    private val application: Application, private val repository: NoteRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(application, repository) as T
        }
        throw IllegalArgumentException("")
    }
}