@file:SuppressLint("ClickableViewAccessibility")
@file:Suppress("DEPRECATION")

package com.ptk.pkeeper.ui

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.ptk.pkeeper.R
import com.ptk.pkeeper.roomdb.entities.NoteEntity
import com.ptk.pkeeper.utility.getFullDate
import com.ptk.pkeeper.vModels.NoteVModel
import kotlinx.android.synthetic.main.activity_note_add.*
import kotlinx.android.synthetic.main.toolbar_centered.*
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance

class NoteAddActivity : AppCompatActivity(), DIAware {
    override val di by di()
    private val noteVModel: NoteVModel by instance()
    private lateinit var tlbToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_add)
        tlbToolbar = findViewById(R.id.tlbToolbar)

        txtLastModifiedDate.text = getString(R.string.today)

        txtLastModifiedDateHour.text = getFullDate()
        setToolbar()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.note_add_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.done -> {
                saveFunction()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun saveFunction() {
        val noteBody = letNoteBody.text.toString()
        if (TextUtils.isEmpty(noteBody)) {
            finish()
        } else {
            val noteEntity = NoteEntity(0, null, noteBody, getFullDate(), null, false)
            noteVModel.insertNote(noteEntity)
            finish()
        }
    }

    private fun setToolbar() {
        setSupportActionBar(tlbToolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //toolbar backarrow
        tlbToolbar.navigationIcon!!.setColorFilter(
            resources.getColor(R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )
        txtToolbarTitle.text = getString(R.string.new_notes)
    }
}