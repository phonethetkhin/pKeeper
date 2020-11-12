package com.ptk.pkeeper.vModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ptk.pkeeper.repository.EncryptionRepository
import com.ptk.pkeeper.roomdb.NoteDB
import com.ptk.pkeeper.roomdb.entities.EncryptionEntity
import kotlinx.coroutines.launch

class EncryptionVModel(application: Application) : AndroidViewModel(application) {
    private val encryptionDao = NoteDB.getNoteDB(application)!!.EncryptionDao()
    private val encryptionRepository = EncryptionRepository(encryptionDao)

    fun insertEncryption(encryptionEntity: EncryptionEntity) = viewModelScope.launch {
        encryptionRepository.insertEncryption(encryptionEntity)
    }

}