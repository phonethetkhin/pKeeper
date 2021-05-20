package com.ptk.pkeeper

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.ptk.pkeeper.roomdb.NoteDB
import com.ptk.pkeeper.utility.getPath
import com.ptk.pkeeper.utility.showToastShort
import ir.androidexception.roomdatabasebackupandrestore.Backup
import ir.androidexception.roomdatabasebackupandrestore.Restore
import kotlinx.android.synthetic.main.fragment_dialog.view.*
import java.io.File


class ToolbarMenuDialogFragment : DialogFragment() {
    val databasePath = Environment.getExternalStorageDirectory().path
    var databaseImportPath = ""
    var status = 0
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val root = RelativeLayout(activity)
        root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        // creating the fullscreen dialog

        // creating the fullscreen dialog
        val dialog = Dialog(activity!!)
        val dialogWindow: Window? = dialog.window

        dialog.setContentView(root)
        dialogWindow!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        val params: WindowManager.LayoutParams = dialogWindow.attributes

        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT

        dialogWindow.attributes = params
        dialogWindow.setGravity(Gravity.TOP)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_dialog, container, false)
        v.txtImport.setOnClickListener {
            status = 1
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermission()
            } else {
                setFileChooseIntent()
            }
        }
        v.imgImport.setOnClickListener {
            status = 1
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermission()
            } else {
                setFileChooseIntent()
            }
        }
        v.txtExport.setOnClickListener {
            status = 2
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermission()
            } else {
                exportFunction()
            }
        }
        v.imgExport.setOnClickListener {
            status = 2
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermission()
            } else {
                exportFunction()
            }
        }
        return v
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermission() {

        if (activity!!.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
            activity!!.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        ) {
            if (status != 0 && status == 1) {
                setFileChooseIntent()
            } else if (status != 0 && status == 2) {
                exportFunction()
            }
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 1
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (status != 0 && status == 1) {
                    setFileChooseIntent()
                } else if (status != 0 && status == 2) {
                    exportFunction()
                }
            } else {
                showToastShort(
                    activity!!,
                    "This application need to read/write storage in order to export backup note memo"
                )
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun exportFunction() {
        Backup.Init()
            .database(NoteDB(requireActivity()))
            .path(databasePath)
            .fileName("pKeeperBackup.memo")
            .onWorkFinishListener { _, _ ->
                // do anything
                showToastShort(activity!!, "Note Successfully Exported with the name pKeeperBackup.memo in external storage")
            }
            .execute()
        this.dismiss()
    }

    private fun importFunction() {
        Restore.Init()
            .database(NoteDB(requireActivity()))
            .backupFilePath(databaseImportPath)
            .onWorkFinishListener { _, _ ->
                // do anything
                showToastShort(activity!!, "Note Imported Successfully")

            }
            .execute()
        this.dismiss()
        activity!!.recreate()
    }

    private fun setFileChooseIntent() {
        Log.d("test", "filechooseFilIntent")
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"   //all files

        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(Intent.createChooser(intent, "Select a File to Import"), 1)

    }

    private fun getPathForOreo(uri: Uri): String {
        val file = File(uri.path!!) //create path from uri

        val split = file.path.split(":") //split the path.

        return split[1] //assign it to a string(your choice).
    }

    private fun getFilePath(uri: Uri?): String? {
        Log.d("test", "setFileData")
        var filePath: String? = ""
        if (uri != null) {
            filePath =
                if (Build.VERSION.SDK_INT >= 27) {
                    getPathForOreo(uri)
                } else {
                    activity!!.getPath(uri)
                }
        } else {
            showToastShort(activity!!, "Error occurred when picking the file")
        }
        return filePath
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == 1) {
                if (resultCode == Activity.RESULT_OK && data != null) {

                    val filePath = getFilePath(data.data)
                    if (filePath != null) {
                        databaseImportPath = filePath
                        importFunction()
                    }
                }
            }

        }
    }
}