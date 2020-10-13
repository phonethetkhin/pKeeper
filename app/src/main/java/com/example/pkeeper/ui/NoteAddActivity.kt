package com.example.pkeeper.ui

import android.graphics.PorterDuff
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.example.pkeeper.R
import com.example.pkeeper.roomdb.entities.NoteEntity
import com.example.pkeeper.utility.getFullDate
import com.example.pkeeper.vModels.NoteVModel
import kotlinx.android.synthetic.main.activity_note_add.*
import kotlinx.android.synthetic.main.toolbar_centered.*

class NoteAddActivity : AppCompatActivity() {
    lateinit var noteVModel: NoteVModel
    private lateinit var tlbToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_add)
        tlbToolbar = findViewById(R.id.tlbToolbar)
        noteVModel = ViewModelProviders.of(this).get(NoteVModel::class.java)

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
            val noteEntity = NoteEntity(0, noteBody, getFullDate(), null)
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