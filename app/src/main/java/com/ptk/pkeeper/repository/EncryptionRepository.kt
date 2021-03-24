package com.ptk.pkeeper.repository

import com.ptk.pkeeper.roomdb.daos.EncryptionDao
import com.ptk.pkeeper.roomdb.entities.EncryptionEntity

class EncryptionRepository(private val encryptionDao: EncryptionDao) {
    suspend fun insertEncryption(encryptionEntity: EncryptionEntity) {
        encryptionDao.insertEncryption(encryptionEntity)
    }

    fun getEncryptionById(noteId: Int): EncryptionEntity {
        return encryptionDao.getEncryptionById(noteId)
    }

    fun deleteEncryptionById(encryptionId: Int) {
        encryptionDao.deleteEncryptionById(encryptionId)
    }
}