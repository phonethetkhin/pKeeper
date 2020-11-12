package com.ptk.pkeeper.repository

import com.ptk.pkeeper.roomdb.daos.EncryptionDao
import com.ptk.pkeeper.roomdb.entities.EncryptionEntity
import com.ptk.pkeeper.roomdb.entities.NoteEntity

class EncryptionRepository(private val encryptionDao: EncryptionDao) {
    suspend fun insertEncryption(encryptionEntity: EncryptionEntity) {
        encryptionDao.insertEncryption(encryptionEntity)
    }
}