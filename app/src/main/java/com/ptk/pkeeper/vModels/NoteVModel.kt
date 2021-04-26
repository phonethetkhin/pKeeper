package com.ptk.pkeeper.vModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ptk.pkeeper.repository.NoteRepository
import com.ptk.pkeeper.roomdb.entities.NoteEntity
import kotlinx.coroutines.launch
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance

class NoteVModel(application: Application) : AndroidViewModel(application), DIAware {
    override val di by di(application)
    private val noteRepo: NoteRepository by instance()

    fun insertNote(noteEntity: NoteEntity) = viewModelScope.launch {
        noteRepo.insertNote(noteEntity)
    }

    fun getAllNotes(): LiveData<List<NoteEntity>>? {
        var noteLiveData: LiveData<List<NoteEntity>>? = null
        viewModelScope.launch {
            noteLiveData = noteRepo.getAllNotes()
        }
        return noteLiveData
    }

    fun updateNote(
        noteId: Int,
        noteTitle: String?,
        noteBody: String,
        lastModifiedDate: String,
        encrypted: Boolean
    ) =
        viewModelScope.launch {
            noteRepo.updateNote(noteId, noteTitle, noteBody, lastModifiedDate, encrypted)
        }

    fun deleteNote(noteId: Int) = viewModelScope.launch {
        noteRepo.deleteNote(noteId)
    }

    fun getNoteById(noteId: Int): NoteEntity {
        return noteRepo.getNoteById(noteId)
    }
}