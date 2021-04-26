package com.ptk.pkeeper.vModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ptk.pkeeper.repository.EncryptionRepository
import com.ptk.pkeeper.roomdb.entities.EncryptionEntity
import kotlinx.coroutines.launch
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance

class EncryptionVModel(application: Application) : AndroidViewModel(application), DIAware {
    override val di by di(application)
    private val encryptionRepository: EncryptionRepository by instance()

    fun insertEncryption(encryptionEntity: EncryptionEntity) = viewModelScope.launch {
        encryptionRepository.insertEncryption(encryptionEntity)
    }

    fun getEncryptionById(noteId: Int): EncryptionEntity {
        return encryptionRepository.getEncryptionById(noteId)
    }

    fun deleteEncryptionById(encryptionId: Int) {
        encryptionRepository.deleteEncryptionById(encryptionId)
    }

}