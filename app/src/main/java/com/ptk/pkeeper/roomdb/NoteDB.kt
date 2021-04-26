package com.ptk.pkeeper.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ptk.pkeeper.roomdb.daos.EncryptionDao
import com.ptk.pkeeper.roomdb.daos.NoteDao
import com.ptk.pkeeper.roomdb.entities.EncryptionEntity
import com.ptk.pkeeper.roomdb.entities.NoteEntity

@Database(
    entities = [NoteEntity::class, EncryptionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDB : RoomDatabase() {
    abstract fun NoteDao(): NoteDao
    abstract fun EncryptionDao(): EncryptionDao

    companion object {
        @Volatile
        private var instance: NoteDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDB(context).also { instance = it }
        }

        private fun buildDB(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                NoteDB::class.java, "restaurants.db"
            )
                .allowMainThreadQueries()
                .build()
    }


}