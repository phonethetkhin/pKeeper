package com.ptk.pkeeper.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.listener.PatternLockViewListener
import com.andrognito.patternlockview.utils.PatternLockUtils
import com.ptk.pkeeper.R
import com.ptk.pkeeper.roomdb.entities.EncryptionEntity
import com.ptk.pkeeper.utility.EncryptionUtil
import com.ptk.pkeeper.utility.getFullDate
import com.ptk.pkeeper.utility.showToastShort
import com.ptk.pkeeper.vModels.EncryptionVModel
import com.ptk.pkeeper.vModels.NoteVModel
import kotlinx.android.synthetic.main.activity_pattern.*
import kotlinx.coroutines.*
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance


class PatternActivity : AppCompatActivity(), DIAware {
    override val di by di()
    var firstPattern = ""
    var secondPattern = ""
    var noteId = 0
    var noteTitle = ""
    var defaultText = ""
    private val noteVModel: NoteVModel by instance()
    private val encryptionVModel: EncryptionVModel by instance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pattern)

        val status = intent!!.getIntExtra("status", 0)
        if (status == 0) {
            noteId = intent!!.getIntExtra("noteId", 0)
            noteTitle = intent!!.getStringExtra("noteTitle")!!
            defaultText = intent!!.getStringExtra("defaultText")!!
            savePattern()
        } else {
            noteId = intent!!.getIntExtra("noteId", 0)
            checkPattern()
        }
    }

    private fun savePattern() {
        txtTitle.text = getString(R.string.set_pattern)
        plvPatternLockView.addPatternLockListener(object : PatternLockViewListener {
            override fun onStarted() {}
            override fun onProgress(progressPattern: MutableList<PatternLockView.Dot>?) {}
            override fun onCleared() {}

            override fun onComplete(pattern: MutableList<PatternLockView.Dot>?) {
                Log.d("pattern", PatternLockUtils.patternToString(plvPatternLockView, pattern))
                if (firstPattern == "") {
                    firstPattern = PatternLockUtils.patternToString(plvPatternLockView, pattern)
                    plvPatternLockView.clearPattern()
                    txtTitle.text = getString(R.string.confirm_pattern)
                } else {
                    secondPattern = PatternLockUtils.patternToString(plvPatternLockView, pattern)
                    if (firstPattern == secondPattern) {
                        plvPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT)
                        showToastShort(this@PatternActivity, "Encrypted Successfully !!!")
                        val encryptionEntity = EncryptionEntity(0, noteId, 1, secondPattern)
                        encryptionVModel.insertEncryption(encryptionEntity)
                        val encryptedText = EncryptionUtil.encrypt(defaultText)
                        noteVModel.updateNote(
                            noteId,
                            noteTitle,
                            encryptedText,
                            getFullDate(),
                            true
                        )
                        startActivity(Intent(this@PatternActivity, MainActivity::class.java))
                        finishAffinity()
                    } else {
                        CoroutineScope(Dispatchers.Main).launch {
                            plvPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG)
                            showToastShort(this@PatternActivity, "Pattern Don't Match !!!")
                            delay(1000L)
                            plvPatternLockView.clearPattern()
                        }
                    }
                }

            }
        })
    }

    private fun checkPattern() {
        txtTitle.text = getString(R.string.draw_pattern)
        val encryptionEntity = encryptionVModel.getEncryptionById(noteId)
        val noteEntity = noteVModel.getNoteById(noteId)

        plvPatternLockView.addPatternLockListener(object : PatternLockViewListener {

            override fun onStarted() {}

            override fun onProgress(progressPattern: MutableList<PatternLockView.Dot>?) {}

            override fun onCleared() {}

            override fun onComplete(pattern: MutableList<PatternLockView.Dot>?) {
                if (PatternLockUtils.patternToString(plvPatternLockView, pattern)
                        .equals(encryptionEntity.password)
                ) {
                    plvPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT)
                    val decryptedText = EncryptionUtil.decrypt(noteEntity.noteBody)
                    noteVModel.updateNote(
                        noteId,
                        null,
                        decryptedText,
                        getFullDate(),
                        false
                    )
                    encryptionVModel.deleteEncryptionById(encryptionEntity.encryptionId)
                    showToastShort(this@PatternActivity, "Decrypted Successfully !!!")
                    finish()
                    startActivity(Intent(this@PatternActivity, MainActivity::class.java))
                } else {
                    CoroutineScope(Dispatchers.Main).launch {
                        plvPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG)
                        showToastShort(this@PatternActivity, "Incorrect Pattern !!!")
                        delay(1000L)
                        plvPatternLockView.clearPattern()
                    }
                }
            }
        })
    }
    fun hiFunction()
    {
        showToastShort(this@PatternActivity,"Hi")
    }
}