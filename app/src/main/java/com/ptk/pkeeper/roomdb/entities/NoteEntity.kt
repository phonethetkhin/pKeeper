package com.ptk.pkeeper.roomdb.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = "tbl_notes", indices = [Index(value = ["note_id"], unique = true)]
)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    val noteId: Int,
    @ColumnInfo(name = "note_title") val noteTitle:String?,
    @ColumnInfo(name = "note_body") val noteBody: String,
    @ColumnInfo(name = "noted_date") val notedDate: String,
    @ColumnInfo(name = "user_id") val userId: Int?,
    @ColumnInfo(name = "encrypted") val encrypted: Boolean

) : Parcelable