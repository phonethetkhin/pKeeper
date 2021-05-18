package com.ptk.pkeeper

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.RelativeLayout
import androidx.fragment.app.DialogFragment


class ToolbarMenuDialogFragment : DialogFragment() {

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
        return inflater.inflate(R.layout.fragment_dialog, container, false)
    }

}