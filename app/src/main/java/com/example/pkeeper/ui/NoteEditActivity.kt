package com.example.pkeeper.ui

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.example.pkeeper.R
import com.example.pkeeper.roomdb.entities.NoteEntity
import com.example.pkeeper.utility.getDateString
import com.example.pkeeper.utility.getFullDate
import com.example.pkeeper.vModels.NoteVModel
import kotlinx.android.synthetic.main.activity_note_edit.*
import kotlinx.android.synthetic.main.toolbar_centered.*

class NoteEditActivity : AppCompatActivity() {
    lateinit var noteVModel: NoteVModel
    private lateinit var tlbToolbar: Toolbar
    var noteId = 0
    var noteModel: NoteEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_edit)
        tlbToolbar = findViewById(R.id.tlbToolbar)
        noteVModel = ViewModelProviders.of(this).get(NoteVModel::class.java)
        noteModel = intent.getParcelableExtra<NoteEntity>("noteModel")
        noteId = noteModel!!.noteId

        //set texts
        txtLastModifiedDateHour.text = noteModel!!.notedDate
        calculateYearDifference(txtLastModifiedDate)
        letNoteBody.setText(noteModel!!.noteBody)
        setToolbar(noteModel!!.noteBody)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.note_add_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.done -> {
                editFunction()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun editFunction() {
        val noteBody = letNoteBody.text.toString()
        if (noteModel!!.noteBody != noteBody) {
            noteVModel.updateNote(noteId, noteBody, getFullDate())
            finish()
        } else {
            finish()
        }
    }

    private fun setToolbar(noteBody: String) {
        setSupportActionBar(tlbToolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //toolbar back arrow
        tlbToolbar.navigationIcon!!.setColorFilter(
            resources.getColor(R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )
        txtToolbarTitle.ellipsize = TextUtils.TruncateAt.END
        txtToolbarTitle.maxEms = 5
        txtToolbarTitle.isSingleLine = true
        txtToolbarTitle.text = noteBody
    }

    @SuppressLint("SetTextI18n")
    private fun calculateYearDifference(txtLastModifiedDate: TextView) {
        //year
        val oldYear: Int =
            getDateString(noteModel!!.notedDate, "yyyy-MM-dd HH:mm:ss", "yyyy")!!.toInt()
        val currentYear: Int = getDateString(getFullDate(), "yyyy-MM-dd HH:mm:ss", "yyyy")!!.toInt()
        val resultYear = currentYear - oldYear

        if (resultYear == 0) {
            calculateMonthDifference(txtLastModifiedDate)
        } else {
            txtLastModifiedDate.text = "$resultYear years ago"
        }
    }

    private fun calculateMonthDifference(txtLastModifiedDate: TextView) {
        //month
        val oldMonth: Int =
            getDateString(noteModel!!.notedDate, "yyyy-MM-dd HH:mm:ss", "MM")!!.toInt()
        val currentMonth: Int = getDateString(getFullDate(), "yyyy-MM-dd HH:mm:ss", "MM")!!.toInt()
        val resultMonth = currentMonth - oldMonth
        if (resultMonth == 0) {
            calculateDateDifference(txtLastModifiedDate)
        } else {
            txtLastModifiedDate.text = "$resultMonth months ago"

        }
    }

    private fun calculateDateDifference(txtLastModifiedDate: TextView) {
        //date
        val oldDate: Int =
            getDateString(noteModel!!.notedDate, "yyyy-MM-dd HH:mm:ss", "dd")!!.toInt()
        val currentDate: Int = getDateString(getFullDate(), "yyyy-MM-dd HH:mm:ss", "dd")!!.toInt()
        val resultDate: Int = currentDate - oldDate

        val resultText: String = when (resultDate) {
            0 -> {
                "Today"
            }
            1 -> {
                "Yesterday"
            }
            else -> {
                "$resultDate days ago"
            }
        }
        txtLastModifiedDate.text = resultText
    }

}