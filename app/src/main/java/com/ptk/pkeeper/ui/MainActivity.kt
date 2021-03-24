@file:SuppressLint("ClickableViewAccessibility")
@file:Suppress("DEPRECATION")

package com.ptk.pkeeper.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Process
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.ptk.pkeeper.R
import com.ptk.pkeeper.adapters.AllNotesAdapter
import com.ptk.pkeeper.roomdb.entities.NoteEntity
import com.ptk.pkeeper.utility.showToastShort
import com.ptk.pkeeper.vModels.NoteVModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_centered.*

class MainActivity : AppCompatActivity() {
    private lateinit var tlbToolbar: Toolbar
    private lateinit var noteVModel: NoteVModel
    private var isSecond = false

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

    override fun onBackPressed() {
        doubleTapToExit()
    }

    /**
     * This method is about checking the user double tap or not.
     * If user double tap within 1 second, kill all processes and exit the app. If not, prompt user to tap again.
     *
     */
    private fun doubleTapToExit() {
        showToastShort(this, getString(R.string.press_again))
        if (isSecond) {
            finishAffinity()
            Process.killProcess(Process.myPid())
        }
        isSecond = true
        Handler().postDelayed({ isSecond = false }, 1000)
    }

}