package com.example.pkeeper.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pkeeper.roomdb.daos.NoteDao
import com.example.pkeeper.roomdb.entities.NoteEntity

@Database(
    entities = [NoteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDB : RoomDatabase() {
    abstract fun NoteDao(): NoteDao

    companion object {
        @Volatile
        var INSTANCE: NoteDB? = null
        fun getNoteDB(context: Context): NoteDB? {
            if (INSTANCE == null) {
                synchronized(this)
                {
                    INSTANCE =
                        Room.databaseBuilder(context, NoteDB::class.java, "notedatabase.db")
                            .allowMainThreadQueries()
                            .build()
                }
            }
            return INSTANCE
        }
    }


}