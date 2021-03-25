@file:Suppress("DEPRECATION")

package com.ptk.pkeeper.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ptk.pkeeper.R
import com.ptk.pkeeper.roomdb.entities.EncryptionEntity
import com.ptk.pkeeper.ui.MainActivity
import com.ptk.pkeeper.utility.*
import com.ptk.pkeeper.vModels.EncryptionVModel
import com.ptk.pkeeper.vModels.NoteVModel
import kotlinx.android.synthetic.main.fragment_pin.view.*

class PinFragment : Fragment() {
    var firstPin = ""
    var secondPin = ""
    var noteId = 0
    var noteTitle = ""
    var defaultText = ""
    private lateinit var noteVModel: NoteVModel
    private lateinit var encryptionVModel: EncryptionVModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_pin, container, false)

        noteVModel = ViewModelProviders.of(activity!!).get(NoteVModel::class.java)
        encryptionVModel = ViewModelProviders.of(activity!!).get(EncryptionVModel::class.java)
        val status = arguments!!.getInt("status", 0)

        //get color state list and set color state to pinview
        val colorStateList = getColorStateList(
            resources.getColor(R.color.colorPrimary),
            resources.getColor(R.color.green)
        )
        v.pnvVerification.setLineColor(colorStateList)
        if (status == 0) {
            noteId = arguments!!.getInt("noteId", 0)
            noteTitle = arguments!!.getString("noteTitle", "")
            defaultText = arguments!!.getString("defaultText", "")
            savePin(v)
        } else {
            noteId = arguments!!.getInt("noteId", 0)
            checkPin(v)
        }
        return v
    }

    private fun savePin(v: View) {
        //listen pin view event changed
        v.txtTitle.text = resources.getString(R.string.set_your_pin)
        v.pnvVerification.inputType = InputType.TYPE_CLASS_NUMBER
        v.pnvVerification.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                Log.d("beforeTextChanged", "beforeTextChanged")
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                Log.d("onTextChanged", "onTextChange")
            }

            override fun afterTextChanged(editable: Editable) {
                val verificationCode: String = v.pnvVerification.text.toString()
                //get user input codes, if length is 4, check verification code is matched with secret pin in database
                if (verificationCode.length == 4) {
                    if (firstPin == "") {
                        firstPin = verificationCode
                        v.pnvVerification.setText("")
                        v.txtTitle.text = resources.getString(R.string.confirm_your_pin)
                        hideSoftKeyboard(v, activity!!)
                        showToastShort(activity!!, "Confirm Your PIN !!!")

                    } else {
                        secondPin = verificationCode
                        if (firstPin == secondPin) {
                            hideSoftKeyboard(v, activity!!)
                            showToastShort(activity!!, "Encrypted Successfully !!!")
                            val encryptionEntity = EncryptionEntity(0, noteId, 0, secondPin)
                            encryptionVModel.insertEncryption(encryptionEntity)
                            val encryptedText = EncryptionUtil.encrypt(defaultText)
                            noteVModel.updateNote(
                                noteId,
                                noteTitle,
                                encryptedText,
                                getFullDate(),
                                true
                            )
                            startActivity(Intent(activity!!, MainActivity::class.java))
                            activity!!.finishAffinity()
                        } else {
                            hideSoftKeyboard(v, activity!!)
                            v.pnvVerification.setLineColor(resources.getColor(R.color.colorWrongLine))
                            showToastShort(activity!!, "PIN Don't Match!!!")

                        }
                    }
                } else {
                    val colorStateList = getColorStateList(
                        resources.getColor(R.color.colorPrimary),
                        resources.getColor(R.color.green)
                    )
                    v.pnvVerification.setLineColor(colorStateList)
                }
            }
        })
    }

    private fun checkPin(v: View) {
        //listen pin view event changed
        v.txtTitle.text = resources.getString(R.string.enter_your_pin)
        v.pnvVerification.inputType =
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
        v.pnvVerification.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                Log.d("beforeTextChanged", "beforeTextChanged")
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                Log.d("onTextChanged", "onTextChange")
            }

            override fun afterTextChanged(editable: Editable) {
                val verificationCode: String = v.pnvVerification.text.toString()
                //get user input codes, if length is 4, check verification code is matched with secret pin in database
                if (verificationCode.length == 4) {
                    val currentEncryptionEntity = encryptionVModel.getEncryptionById(noteId)
                    val currentNoteEntity = noteVModel.getNoteById(noteId)
                    if (verificationCode == currentEncryptionEntity.password) {
                        val defaultText = currentNoteEntity.noteBody
                        val decryptedText = EncryptionUtil.decrypt(defaultText)
                        noteVModel.updateNote(
                            noteId,
                            null,
                            decryptedText,
                            getFullDate(),
                            false
                        )
                        hideSoftKeyboard(v, activity!!)
                        encryptionVModel.deleteEncryptionById(currentEncryptionEntity.encryptionId)
                        showToastShort(activity!!, "Decrypted Successfully !!!")
                        activity!!.finish()
                        activity!!.startActivity(Intent(activity!!, MainActivity::class.java))
                    } else {
                        hideSoftKeyboard(v, activity!!)
                        v.pnvVerification.setLineColor(resources.getColor(R.color.colorWrongLine))
                        showToastShort(activity!!, "Incorrect PIN !!!")
                    }
                }
                else
                {
                    val colorStateList = getColorStateList(
                        resources.getColor(R.color.colorPrimary),
                        resources.getColor(R.color.green)
                    )
                    v.pnvVerification.setLineColor(colorStateList)
                }
            }

        })
    }

}