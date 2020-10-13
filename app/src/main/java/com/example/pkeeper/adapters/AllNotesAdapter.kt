@file:SuppressLint("ClickableViewAccessibility")

package com.example.pkeeper.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.pkeeper.R
import com.example.pkeeper.`interface`.OnSwipeTouchListener
import com.example.pkeeper.roomdb.entities.NoteEntity
import com.example.pkeeper.ui.NoteEditActivity
import com.example.pkeeper.utility.getDateString
import com.example.pkeeper.vModels.NoteVModel


class AllNotesAdapter(val app: FragmentActivity, var noteList: List<NoteEntity>) :
    RecyclerView.Adapter<AllNotesAdapter.ViewHolder>() {
    private lateinit var alertDialog: AlertDialog
    private lateinit var noteVModel: NoteVModel

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var txtNoteInitial: TextView = v.findViewById(R.id.txtNoteInitial)
        var txtModifiedDate: TextView = v.findViewById(R.id.txtModifiedDate)
        var cslNote: ConstraintLayout = v.findViewById(R.id.cslNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_all_notes, parent, false)
        noteVModel = ViewModelProviders.of(app).get(NoteVModel::class.java)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtNoteInitial.text = noteList[position].noteBody
        val date = getDateString(noteList[position].notedDate, "yyyy-MM-dd HH:mm:ss", "dd-MM")
        holder.txtModifiedDate.text = date
        holder.cslNote.setOnClickListener {
            val i = Intent(app, NoteEditActivity::class.java)
            val b = Bundle()
            b.putParcelable("noteModel", noteList[position])
            i.putExtras(b)
            app.startActivity(i)
        }
        holder.cslNote.setOnTouchListener(
            object : OnSwipeTouchListener(app) {

                override fun onSwipeTop() {
                    Log.d("test", "Top")
                    super.onSwipeTop()
                }

                override fun onSwipeBottom() {
                    Log.d("test", "Bottom")
                    super.onSwipeBottom()
                }

                override fun onSwipeLeft() {
                    Log.d("test", "Left")
                    super.onSwipeLeft()
                }

                override fun onSwipeRight() {
                    Log.d("test", "Right")
                    super.onSwipeRight()
                }
            })
        holder.cslNote.setOnLongClickListener {
            addingDialog(noteList[position].noteId)
            true
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }


    /**
     * This function is about adding the dialog to confirm exit
     *
     */
    private fun addingDialog(noteId: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(app)
        builder.setTitle("Are you sure you want to delete this note?")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes") { _, _ ->
            noteVModel.deleteNote(noteId)
        }
        builder.setNegativeButton("Cancel") { _, _ -> }
        alertDialog = builder.create()
        alertDialog.show()
    }

    private fun deleteNote() {

    }
}