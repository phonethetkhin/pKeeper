@file:SuppressLint("ClickableViewAccessibility")
@file:Suppress("DEPRECATION", "UNCHECKED_CAST")

package com.ptk.pkeeper.adapters

import android.R.attr.label
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.ptk.pkeeper.R
import com.ptk.pkeeper.roomdb.entities.NoteEntity
import com.ptk.pkeeper.ui.NoteEditActivity
import com.ptk.pkeeper.utility.deleteDialog
import com.ptk.pkeeper.utility.encryptedDialog
import com.ptk.pkeeper.utility.getDateString
import com.ptk.pkeeper.vModels.EncryptionVModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_all_notes.*
import java.util.*
import kotlin.collections.ArrayList


class AllNotesAdapter(val app: FragmentActivity, var noteList: ArrayList<NoteEntity>) :
    RecyclerView.Adapter<AllNotesAdapter.ViewHolder>(), Filterable {
    private val valueFilter = ValueFilter()
    private val filterNoteList = ArrayList(noteList)

    private lateinit var encryptionVModel: EncryptionVModel

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_all_notes, parent, false)
        encryptionVModel = ViewModelProvider(app).get(EncryptionVModel::class.java)
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

        holder.cslNote.setOnClickListener {
            if (noteList[position].encrypted) {
                val encryptionEntity = encryptionVModel.getEncryptionById(noteList[position].noteId)
                encryptedDialog(app, noteList, position, encryptionEntity.lockType)
            } else {
                setIntent(position)
            }
        }

        holder.cslNote.setOnLongClickListener {
            app.copyToClipboard(noteList[position].noteBody)
            return@setOnLongClickListener true
        }

        holder.imgDelete.setOnClickListener {
            deleteDialog(noteList[position].noteId, app, false)
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }
    fun Context.copyToClipboard(text: CharSequence){
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label",text)
        clipboard.setPrimaryClip(clip)
    }
    private fun setIntent(position: Int) {
        val i = Intent(app, NoteEditActivity::class.java)
        val b = Bundle()
        b.putParcelable("noteModel", noteList[position])
        i.putExtras(b)
        app.startActivity(i)
    }

    inner class ValueFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val results = FilterResults()
            val filterList: MutableList<NoteEntity> = ArrayList()
            if (constraint.isNotEmpty()) {
                for (i in filterNoteList.indices) {
                    if (filterNoteList[i].encrypted && filterNoteList[i].noteTitle != null) {
                        if (filterNoteList[i].noteTitle!!.toUpperCase(Locale.ROOT).contains(
                                constraint.toString().toUpperCase(
                                    Locale.ROOT
                                )
                            )
                        ) {
                            filterList.add(filterNoteList[i])
                        }
                    } else {
                        if (filterNoteList[i].noteBody.toUpperCase(Locale.ROOT).contains(
                                constraint.toString().toUpperCase(
                                    Locale.ROOT
                                )
                            )
                        ) {
                            filterList.add(filterNoteList[i])
                        }
                    }

                }
                results.count = filterList.size
                results.values = filterList
            } else {
                results.count = filterNoteList.size
                results.values = filterNoteList
            }
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            noteList = results.values as ArrayList<NoteEntity>
            notifyDataSetChanged()
        }
    }

    override fun getFilter(): Filter {
        return valueFilter
    }
}