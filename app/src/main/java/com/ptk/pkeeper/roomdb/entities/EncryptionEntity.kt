package com.ptk.pkeeper.roomdb.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = "tbl_encryption", indices = [Index(value = ["encryption_id"], unique = true)]
)
data class EncryptionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "encryption_id")
    val encryptionId: Int,
    @ColumnInfo(name = "note_id") val noteId: Int,
    @ColumnInfo(name = "lock_type") val lockType: Int,
    @ColumnInfo(name = "password") val password: String
) : Parcelable