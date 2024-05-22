package ir.alirezasoheili.notebookapplication.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "notes_tbl")
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("note_id") val id: Int,
    @ColumnInfo("note_title") val title: String,
    @ColumnInfo("note_body") val body: String
) : Parcelable
