package ir.alirezasoheili.notebookapplication.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ir.alirezasoheili.notebookapplication.model.Note

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM notes_tbl ORDER BY note_id DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notes_tbl WHERE note_title LIKE '%' || :query || '%' OR note_body LIKE '%' || :query || '%' ORDER BY note_id DESC")
    fun search(query: String?): LiveData<List<Note>>
}