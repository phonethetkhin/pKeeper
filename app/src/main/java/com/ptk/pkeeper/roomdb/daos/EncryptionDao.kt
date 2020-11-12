package com.ptk.pkeeper.roomdb.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.ptk.pkeeper.roomdb.entities.EncryptionEntity

@Dao
interface EncryptionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEncryption(encryptionEntity: EncryptionEntity)

}