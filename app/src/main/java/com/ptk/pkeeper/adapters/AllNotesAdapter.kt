@file:SuppressLint("ClickableViewAccessibility")
@file:Suppress("DEPRECATION", "UNCHECKED_CAST")

package com.ptk.pkeeper.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ptk.pkeeper.R
import com.ptk.pkeeper.roomdb.entities.NoteEntity
import com.ptk.pkeeper.ui.NoteEditActivity
import com.ptk.pkeeper.utility.*
import com.ptk.pkeeper.vModels.EncryptionVModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item_all_notes.*
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance
import java.util.*
import kotlin.collections.ArrayList


class AllNotesAdapter(val app: FragmentActivity, var noteList: ArrayList<NoteEntity>) :
    RecyclerView.Adapter<AllNotesAdapter.ViewHolder>(), Filterable, DIAware {
    override val di by di(app)

    private val valueFilter: ValueFilter by instance()
    private val filterNoteList = ArrayList(noteList)

    private val encryptionVModel: EncryptionVModel by instance()

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_all_notes, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val encryptionEntity = encryptionVModel.getEncryptionById(noteList[position].noteId)

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
                encryptedDialog(
                    app,
                    noteList,
                    position,
                    encryptionEntity.lockType,
                    "This Note is Encrypted, Do you want to Decrypt this Note?"
                )
            } else {
                setIntent(position)
            }
        }

        holder.cslNote.setOnLongClickListener {
            if (noteList[position].encrypted) {
                encryptedDialogForCopy(
                    app,
                    noteList,
                    position,
                    encryptionEntity.lockType,
                    "This note is encrypted !!! You can decrypt this note or just copy encrypted text"
                )
            } else {
                showToastShort(app, "Copied to Clipboard")
                app.copyToClipboard(noteList[position].noteBody)
            }
            return@setOnLongClickListener true
        }

        holder.imgDelete.setOnClickListener {
            if (noteList[position].encrypted) {
                encryptedDialog(
                    app,
                    noteList,
                    position,
                    encryptionEntity.lockType,
                    "You can't delete Encrypted notes. In order to delete, you must first Decrypt this Note !!!"
                )
            } else {
                deleteDialog(noteList[position].noteId, app, false)
            }
        }
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