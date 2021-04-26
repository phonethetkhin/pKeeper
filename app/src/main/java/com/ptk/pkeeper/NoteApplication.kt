package com.ptk.pkeeper

import android.app.Application
import com.ptk.pkeeper.repository.EncryptionRepository
import com.ptk.pkeeper.repository.NoteRepository
import com.ptk.pkeeper.roomdb.NoteDB
import com.ptk.pkeeper.ui.NoteEditActivity
import com.ptk.pkeeper.ui.PatternActivity
import com.ptk.pkeeper.vModels.EncryptionVModel
import com.ptk.pkeeper.vModels.NoteVModel
import org.kodein.di.*
import org.kodein.di.android.x.androidXModule

class NoteApplication : Application(), DIAware {

    override val di by DI.lazy {
        import(androidXModule(this@NoteApplication))
        bind() from singleton { NoteDB(instance()) }

        bind() from singleton { instance<NoteDB>().EncryptionDao() }
        bind() from singleton { EncryptionRepository(instance()) }
        bind() from provider { EncryptionVModel(instance()) }

        bind() from singleton { instance<NoteDB>().NoteDao() }
        bind() from singleton { NoteRepository(instance()) }
        bind() from provider { NoteVModel(instance()) }
    }
}