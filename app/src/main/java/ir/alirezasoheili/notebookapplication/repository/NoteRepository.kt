package ir.alirezasoheili.notebookapplication.repository

import ir.alirezasoheili.notebookapplication.database.NoteDao
import ir.alirezasoheili.notebookapplication.database.NoteDatabase
import ir.alirezasoheili.notebookapplication.model.Note

class NoteRepository(database: NoteDatabase) {

    private val dao: NoteDao = database.getNoteDao()

    suspend fun insertNote(note: Note) {
        dao.insert(note)
    }

    suspend fun updateNote(note: Note) {
        dao.update(note)
    }

    suspend fun deleteNote(note: Note) {
        dao.delete(note)
    }

    suspend fun deleteAllNotes() {
        dao.deleteAll()
    }

    fun getAllNotes() = dao.getAllNotes()

    fun searchNote(query: String?) = dao.search(query)
}