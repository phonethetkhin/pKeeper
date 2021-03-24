@file:SuppressLint("ClickableViewAccessibility")
@file:Suppress("DEPRECATION")

package com.ptk.pkeeper.ui

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ptk.pkeeper.R
import com.ptk.pkeeper.roomdb.entities.NoteEntity
import com.ptk.pkeeper.utility.deleteDialog
import com.ptk.pkeeper.utility.getDateString
import com.ptk.pkeeper.utility.getFullDate
import com.ptk.pkeeper.utility.showToastShort
import com.ptk.pkeeper.vModels.EncryptionVModel
import com.ptk.pkeeper.vModels.NoteVModel
import kotlinx.android.synthetic.main.activity_note_edit.*
import kotlinx.android.synthetic.main.toolbar_centered.*


class NoteEditActivity : AppCompatActivity() {
    lateinit var noteVModel: NoteVModel
    lateinit var encryptionVModel: EncryptionVModel
    private lateinit var tlbToolbar: Toolbar
    private lateinit var bnvBottomNavigation: BottomNavigationView
    var noteId = 0
    var noteTitle: String? = null
    var encryptedStatus = false
    var noteModel: NoteEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_edit)
        tlbToolbar = findViewById(R.id.tlbToolbar)
        bnvBottomNavigation = findViewById(R.id.bnvBottomNavigation)
        noteVModel = ViewModelProvider(this).get(NoteVModel::class.java)
        encryptionVModel = ViewModelProvider(this).get(EncryptionVModel::class.java)
        noteModel = intent.getParcelableExtra<NoteEntity>("noteModel")

        noteId = noteModel!!.noteId
        noteTitle = noteModel!!.noteTitle
        encryptedStatus = noteModel!!.encrypted
        if (encryptedStatus) {
            bnvBottomNavigation.menu.findItem(R.id.nav_encrypt).isVisible = false
            bnvBottomNavigation.menu.findItem(R.id.nav_decrypt).isVisible = true
        } else {
            bnvBottomNavigation.menu.findItem(R.id.nav_encrypt).isVisible = true
            bnvBottomNavigation.menu.findItem(R.id.nav_decrypt).isVisible = false
        }

        //set texts
        txtLastModifiedDateHour.text = noteModel!!.notedDate
        calculateYearDifference(txtLastModifiedDate)
        letNoteBody.setText(noteModel!!.noteBody)
        setToolbar(noteModel!!.noteBody)

        bnvBottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_encrypt -> {
                    showEncryptMethodSelectionDialog()
                }
                R.id.nav_delete -> {
                    deleteDialog(noteId, this, true)
                }
                R.id.nav_decrypt -> {
                    val encryptionEntity = encryptionVModel.getEncryptionById(noteId)
                    Log.d("err","$noteId")
                    Log.d("err","${encryptionEntity.lockType}")

                    when (encryptionEntity.lockType) {
                        0 -> {
                            Log.d("err","pin")

                            val intent = Intent(this, PINActivity::class.java)
                            intent.putExtra("status", 1)
                            intent.putExtra("noteId", noteId)
                            this.finish()
                            startActivity(intent)
                        }
                        1 -> {
                            Log.d("err","pattern")
                            val intent = Intent(this, PatternActivity::class.java)
                            intent.putExtra("status", 1)
                            intent.putExtra("noteId", noteId)
                            this.finish()
                            startActivity(intent)
                        }
                    }
                }
            }
            true
        }
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
            noteVModel.updateNote(noteId, noteTitle, noteBody, getFullDate(), false)
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

    private fun showEncryptMethodSelectionDialog() {
        val encryptMethods = arrayOf("PIN", "Pattern", "FingerPrint")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Encryption Method")
        builder.setItems(encryptMethods) { _, which ->
            when (which) {
                0 -> {
                    showCustomDialog(PINActivity())
                }
                1 -> {
                    showCustomDialog(PatternActivity())
                }
                2 -> {
                    showToastShort(this, "FingerPrint")
                }

            }
        }
        builder.show()
    }

    private fun showCustomDialog(activity: AppCompatActivity) {
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.custom_title_dialog, null)
        dialogBuilder.setView(dialogView)
        val edtNoteTitle = dialogView.findViewById(R.id.edtNoteTitle) as EditText
        dialogBuilder.setTitle("Enter Note Title")
        dialogBuilder.setMessage("In order to encrypt, you need to specify note title to remember which note you encrypted.")
        dialogBuilder.setPositiveButton("Done",
            DialogInterface.OnClickListener { _, _ ->
                noteTitle = edtNoteTitle.text.toString()
                val intent = Intent(this, activity::class.java)
                intent.putExtra("status", 0)
                intent.putExtra("noteId", noteId)
                intent.putExtra("noteTitle", noteTitle)
                intent.putExtra("defaultText", letNoteBody.text.toString())
                this.finish()
                startActivity(intent)
            })
        dialogBuilder.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { _, _ ->
                //pass
            })
        val b: AlertDialog = dialogBuilder.create()
        b.show()
    }

}