package com.ptk.pkeeper.repository

import androidx.lifecycle.LiveData
import com.ptk.pkeeper.roomdb.daos.NoteDao
import com.ptk.pkeeper.roomdb.entities.NoteEntity

class NoteRepository(private val noteDao: NoteDao) {

    suspend fun insertNote(noteEntity: NoteEntity) {
        noteDao.insertNote(noteEntity)
    }

    fun getAllNotes(): LiveData<List<NoteEntity>>? {
        return noteDao.getAllNotes()
    }

    suspend fun updateNote(
        noteId: Int,
        noteTitle: String?,
        noteBody: String,
        lastModifiedDate: String,
        encrypted: Boolean
    ) {
        noteDao.updateNote(noteId, noteTitle, noteBody, lastModifiedDate, encrypted)
    }

    suspend fun deleteNote(noteId: Int) {
        noteDao.deleteNote(noteId)
    }

    fun getNoteById(noteId: Int) : NoteEntity {
        return noteDao.getNoteById(noteId)
    }
}