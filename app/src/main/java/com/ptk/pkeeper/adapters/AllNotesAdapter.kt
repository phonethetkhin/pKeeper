@file:SuppressLint("ClickableViewAccessibility")
@file:Suppress("DEPRECATION")

package com.ptk.pkeeper.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ptk.pkeeper.R
import com.ptk.pkeeper.`interface`.OnSwipeTouchListener
import com.ptk.pkeeper.roomdb.entities.NoteEntity
import com.ptk.pkeeper.ui.NoteEditActivity
import com.ptk.pkeeper.utility.deleteDialog
import com.ptk.pkeeper.utility.encryptedDialog
import com.ptk.pkeeper.utility.getDateString


class AllNotesAdapter(val app: FragmentActivity, var noteList: List<NoteEntity>) :
    RecyclerView.Adapter<AllNotesAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var imgLock: ImageView = v.findViewById(R.id.imgLock)
        var txtNoteInitial: TextView = v.findViewById(R.id.txtNoteInitial)
        var txtModifiedDate: TextView = v.findViewById(R.id.txtModifiedDate)
        var cslNote: ConstraintLayout = v.findViewById(R.id.cslNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_all_notes, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (noteList[position].encrypted) {
            holder.imgLock.visibility = View.VISIBLE
        } else {
            holder.imgLock.visibility = View.GONE
        }
        if (noteList[position].noteTitle != null) {
            holder.txtNoteInitial.text = noteList[position].noteTitle
        } else {
            holder.txtNoteInitial.text = noteList[position].noteBody
        }
        val date = getDateString(noteList[position].notedDate, "yyyy-MM-dd HH:mm:ss", "dd-MM")
        holder.txtModifiedDate.text = date

        holder.cslNote.setOnTouchListener(
            object : OnSwipeTouchListener(app) {

                override fun onClick() {
                    if (noteList[position].encrypted) {
                        encryptedDialog(app)
                    } else {
                        setIntent(position)
                    }
                    super.onClick()
                }

                override fun onLongClick() {
                    deleteDialog(noteList[position].noteId, app, false)
                    super.onLongClick()
                }

                override fun onSwipeLeft() {
                    deleteDialog(noteList[position].noteId, app, false)
                    super.onSwipeLeft()
                }

                override fun onSwipeRight() {
                    if (noteList[position].encrypted) {
                        encryptedDialog(app)
                    } else {
                        setIntent(position)
                    }
                    super.onSwipeRight()
                }
            })
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    private fun setIntent(position: Int) {
        val i = Intent(app, NoteEditActivity::class.java)
        val b = Bundle()
        b.putParcelable("noteModel", noteList[position])
        i.putExtras(b)
        app.startActivity(i)
    }
}