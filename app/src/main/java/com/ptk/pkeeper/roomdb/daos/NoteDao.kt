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

    @Query("UPDATE tbl_notes SET note_body=:noteBody,noted_date=:lastModifiedDate WHERE note_id=:noteId")
    suspend fun updateNote(noteId: Int, noteBody: String, lastModifiedDate: String)

    @Query("DELETE FROM tbl_notes WHERE note_id=:noteId")
    suspend fun deleteNote(noteId: Int)

}