package com.ptk.pkeeper.roomdb.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ptk.pkeeper.roomdb.entities.NoteEntity

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(noteEntity: NoteEntity)

    @Query("SELECT * FROM tbl_notes")
    fun getAllNotes(): LiveData<List<NoteEntity>>

    @Query("UPDATE tbl_notes SET note_title=:noteTitle, note_body=:noteBody,noted_date=:lastModifiedDate,encrypted=:encrypted WHERE note_id=:noteId")
    suspend fun updateNote(
        noteId: Int,
        noteTitle: String?,
        noteBody: String,
        lastModifiedDate: String,
        encrypted: Boolean
    )

    @Query("DELETE FROM tbl_notes WHERE note_id=:noteId")
    suspend fun deleteNote(noteId: Int)

    @Query("SELECT * FROM tbl_notes WHERE note_id=:noteId")
    fun getNoteById(noteId: Int): NoteEntity
}