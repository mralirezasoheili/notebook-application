package ir.alirezasoheili.notebookapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ir.alirezasoheili.notebookapplication.model.Note

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun getNoteDao(): NoteDao

    companion object {
        private const val DATABASE_NAME = "notes_db"

        @Volatile
        private var instance: NoteDatabase? = null

        private val lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context, NoteDatabase::class.java, DATABASE_NAME
        ).build()
    }
}