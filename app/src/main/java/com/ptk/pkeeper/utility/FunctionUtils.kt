@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.ptk.pkeeper.utility

import android.annotation.SuppressLint
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.ptk.pkeeper.vModels.NoteVModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * This function is about getting the current system time.
 *
 * @return
 */
fun getFullDate(): String {
    val c = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return dateFormat.format(c.time)
}

@SuppressLint("SimpleDateFormat")
fun getDateString(
    stringDate: String?,
    stringDateFormat: String?,
    returnDateFormat: String?
): String? {
    val date = SimpleDateFormat(stringDateFormat).parse(stringDate)
    return SimpleDateFormat(returnDateFormat).format(date)
}

/**
 * This function is about adding the dialog to confirm exit
 *
 */
fun addingDialog(noteId: Int, app: FragmentActivity, finish: Boolean) {
    val alertDialog: AlertDialog
    val noteVModel = ViewModelProviders.of(app).get(NoteVModel::class.java)
    val builder: AlertDialog.Builder = AlertDialog.Builder(app)
    builder.setTitle("Are you sure you want to delete this note?")
    builder.setCancelable(false)
    builder.setPositiveButton("Yes") { _, _ ->
        if (finish) {
            noteVModel.deleteNote(noteId)
            app.finish()
        } else {
            noteVModel.deleteNote(noteId)
        }
    }
    builder.setNegativeButton("Cancel") { _, _ -> }
    alertDialog = builder.create()
    alertDialog.show()
}