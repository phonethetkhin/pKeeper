package com.example.pkeeper.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MotionEventCompat
import androidx.lifecycle.ViewModelProviders
import com.example.pkeeper.R
import com.example.pkeeper.adapters.AllNotesAdapter
import com.example.pkeeper.roomdb.entities.NoteEntity
import com.example.pkeeper.vModels.NoteVModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_centered.*

class MainActivity : AppCompatActivity() {
    private lateinit var tlbToolbar: Toolbar
    private lateinit var noteVModel: NoteVModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tlbToolbar = findViewById(R.id.tlbToolbar)
        noteVModel = ViewModelProviders.of(this).get(NoteVModel::class.java)
        setToolbar()
        setAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.add -> startActivity(Intent(this, NoteAddActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setToolbar() {
        setSupportActionBar(tlbToolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        txtToolbarTitle.text = getString(R.string.all_notes)
    }

    private fun setAdapter() {
        rcvAllNotes.setHasFixedSize(true)
        noteVModel.getAllNotes()!!.observe(this, {
            val sortedList = it.sortedWith(compareBy(NoteEntity::notedDate))
            val adapter = AllNotesAdapter(this, sortedList)
            rcvAllNotes.adapter = adapter
        })
    }


}