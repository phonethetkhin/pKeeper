@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")

package com.ptk.pkeeper.utility

import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.ptk.pkeeper.roomdb.entities.NoteEntity
import com.ptk.pkeeper.ui.NoteEditActivity
import com.ptk.pkeeper.ui.PINActivity
import com.ptk.pkeeper.ui.PatternActivity
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
fun deleteDialog(noteId: Int, app: FragmentActivity, finish: Boolean) {
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

@SuppressLint("UseCompatLoadingForDrawables")
fun encryptedDialog(
    app: FragmentActivity,
    noteList: List<NoteEntity>,
    position: Int,
    lockType: Int,
    message: String
) {

    // setup the alert builder
    val builder = AlertDialog.Builder(app)
    builder.setTitle("Notice")
    builder.setMessage(message)

    // add the buttons
    builder.setNegativeButton("Cancel") { _, _ -> }
    builder.setPositiveButton("Decrypt") { _, _ ->
        when (lockType) {
            0 -> {
                Log.d("err", "pin")

                val intent = Intent(app, PINActivity::class.java)
                intent.putExtra("status", 1)
                intent.putExtra("noteId", noteList[position].noteId)
                app.startActivity(intent)
            }
            1 -> {
                Log.d("err", "pattern")
                val intent = Intent(app, PatternActivity::class.java)
                intent.putExtra("status", 1)
                intent.putExtra("noteId", noteList[position].noteId)
                app.startActivity(intent)
            }
        }
    }
    builder.setNeutralButton("Just Watch") { _, _ ->
        val i = Intent(app, NoteEditActivity::class.java)
        val b = Bundle()
        b.putParcelable("noteModel", noteList[position])
        i.putExtras(b)
        app.startActivity(i)
    }

    // create and show the alert dialog
    val dialog = builder.create()
    dialog.show()

    val btnPositive: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
    val btnNegative: Button = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

    btnPositive.setTextColor(app.resources.getColor(R.color.holo_green_dark))
    btnPositive.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)

    val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
    layoutParams.weight = 10f
    btnPositive.layoutParams = layoutParams
    btnNegative.layoutParams = layoutParams
    val drawable: Drawable = app.resources.getDrawable(R.drawable.ic_lock_lock)
    drawable.setBounds(
        (drawable.intrinsicWidth * 0.5).toInt(),
        0, (drawable.intrinsicWidth * 1.5).toInt(),
        drawable.intrinsicHeight
    )
    btnPositive.setCompoundDrawables(drawable, null, null, null)
    btnPositive.compoundDrawablePadding = 30
}

@SuppressLint("UseCompatLoadingForDrawables")
fun encryptedDialogForCopy(
    app: FragmentActivity,
    noteList: List<NoteEntity>,
    position: Int,
    lockType: Int,
    message: String
) {

    // setup the alert builder
    val builder = AlertDialog.Builder(app)
    builder.setTitle("Notice")
    builder.setMessage(message)

    // add the buttons
    builder.setNegativeButton("Cancel") { _, _ -> }
    builder.setPositiveButton("Decrypt") { _, _ ->
        when (lockType) {
            0 -> {
                Log.d("err", "pin")

                val intent = Intent(app, PINActivity::class.java)
                intent.putExtra("status", 1)
                intent.putExtra("noteId", noteList[position].noteId)
                app.startActivity(intent)
            }
            1 -> {
                Log.d("err", "pattern")
                val intent = Intent(app, PatternActivity::class.java)
                intent.putExtra("status", 1)
                intent.putExtra("noteId", noteList[position].noteId)
                app.startActivity(intent)
            }
        }
    }
    builder.setNeutralButton("Just Copy") { _, _ ->
        showToastShort(app, "Copied to Clipboard")
        app.copyToClipboard(noteList[position].noteBody)
    }

    // create and show the alert dialog
    val dialog = builder.create()
    dialog.show()

    val btnPositive: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
    val btnNegative: Button = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

    btnPositive.setTextColor(app.resources.getColor(R.color.holo_green_dark))
    btnPositive.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)

    val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
    layoutParams.weight = 10f
    btnPositive.layoutParams = layoutParams
    btnNegative.layoutParams = layoutParams
    val drawable: Drawable = app.resources.getDrawable(R.drawable.ic_lock_lock)
    drawable.setBounds(
        (drawable.intrinsicWidth * 0.5).toInt(),
        0, (drawable.intrinsicWidth * 1.5).toInt(),
        drawable.intrinsicHeight
    )
    btnPositive.setCompoundDrawables(drawable, null, null, null)
    btnPositive.compoundDrawablePadding = 30
}

fun Context.copyToClipboard(text: CharSequence) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label", text)
    clipboard.setPrimaryClip(clip)
}

@SuppressLint("UseCompatLoadingForDrawables")
fun encryptedDialogForNoteEdit(
    app: FragmentActivity,
    lockType: Int,
    noteId: Int,
    message: String
) {

    // setup the alert builder
    val builder = AlertDialog.Builder(app)
    builder.setTitle("Notice")
    builder.setMessage(message)

    // add the buttons
    builder.setNegativeButton("Cancel") { _, _ -> }
    builder.setPositiveButton("Decrypt") { _, _ ->
        when (lockType) {
            0 -> {
                Log.d("err", "pin")

                val intent = Intent(app, PINActivity::class.java)
                intent.putExtra("status", 1)
                intent.putExtra("noteId", noteId)
                app.startActivity(intent)
            }
            1 -> {
                Log.d("err", "pattern")
                val intent = Intent(app, PatternActivity::class.java)
                intent.putExtra("status", 1)
                intent.putExtra("noteId", noteId)
                app.startActivity(intent)
            }
        }
    }

    // create and show the alert dialog
    val dialog = builder.create()
    dialog.show()

    val btnPositive: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
    val btnNegative: Button = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

    btnPositive.setTextColor(app.resources.getColor(R.color.holo_green_dark))
    btnPositive.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)

    val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
    layoutParams.weight = 10f
    btnPositive.layoutParams = layoutParams
    btnNegative.layoutParams = layoutParams
    val drawable: Drawable = app.resources.getDrawable(R.drawable.ic_lock_lock)
    drawable.setBounds(
        (drawable.intrinsicWidth * 1.5).toInt(),
        0, (drawable.intrinsicWidth * 1.5).toInt(),
        drawable.intrinsicHeight
    )
    btnPositive.setCompoundDrawables(drawable, null, null, null)
    btnPositive.compoundDrawablePadding = 30
}

/**
 * This function is about setting the fragments.
 *
 * @param fragmentManager (FragmentManager)
 * @param fragment (Fragment)
 * @param backStack (Boolean)
 * @param container (Int)
 */
fun setFragment(
    fragmentManager: FragmentManager, fragment: Fragment, backStack: Boolean,
    container: Int
) {

    val transaction: FragmentTransaction = fragmentManager.beginTransaction()

    if (!backStack) {
        transaction.replace(container, fragment).commit()
    } else {
        transaction.replace(container, fragment).addToBackStack(null).commit()
    }
}

/**
 * This function is about getting the color state and return color state list
 *
 * @param enabledColor(Int)
 * @param disabledColor (Int)
 * @return ColorStateList
 */
fun getColorStateList(enabledColor: Int, disabledColor: Int): ColorStateList {
    return ColorStateList(
        arrayOf(
            intArrayOf(R.attr.state_enabled), intArrayOf(-R.attr.state_enabled)
        ), intArrayOf(
            enabledColor, disabledColor

        )
    )
}

fun showSoftKeyboard(v: View, activity: FragmentActivity) {
    if (v.requestFocus()) {
        (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }
}

fun hideSoftKeyboard(v: View, activity: FragmentActivity) {
    val inputMethodManager =
        activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
}

/**
 * This function is about showing the toast to the user.
 *
 * @param context(Context)
 * @param text (String)
 * @param length (Int)
 */
fun showToastShort(context: Context, text: String?) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

/**
 * This fragment is about
 *
 * @param fragmentManager
 * @param fragment
 * @param backStack
 * @param container
 * @param bundle
 */
fun setFragmentByBundle(
    fragmentManager: FragmentManager, fragment: Fragment, backStack: Boolean,
    container: Int, bundle: Bundle
) {

    val transaction: FragmentTransaction = fragmentManager.beginTransaction()
    fragment.arguments = bundle

    if (backStack) {
        transaction.replace(container, fragment).commit()
    } else {
        transaction.replace(container, fragment).addToBackStack(null).commit()
    }

}