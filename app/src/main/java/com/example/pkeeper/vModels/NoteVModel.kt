package com.example.pkeeper.vModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.pkeeper.repository.NoteRepository
import com.example.pkeeper.roomdb.NoteDB
import com.example.pkeeper.roomdb.entities.NoteEntity
import kotlinx.coroutines.launch

class NoteVModel(application: Application) : AndroidViewModel(application) {
    private val noteDao = NoteDB.getNoteDB(application)!!.NoteDao()
    private val noteRepo = NoteRepository(noteDao)

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

    fun updateNote(noteId: Int, noteBody: String, lastModifiedDate: String) =
        viewModelScope.launch {
            noteRepo.updateNote(noteId, noteBody, lastModifiedDate)
        }

    fun deleteNote(noteId: Int) = viewModelScope.launch {
        noteRepo.deleteNote(noteId)
    }
}