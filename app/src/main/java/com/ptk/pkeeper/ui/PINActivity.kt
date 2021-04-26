package com.ptk.pkeeper.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ptk.pkeeper.R
import com.ptk.pkeeper.fragment.PinFragment
import com.ptk.pkeeper.utility.setFragmentByBundle
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.instance

class PINActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin)

        val status = intent!!.getIntExtra("status", 0)
        if (status == 0) {
            val noteId = intent!!.getIntExtra("noteId", 0)
            val noteTitle: String? = intent!!.getStringExtra("noteTitle")
            val defaultText: String? = intent!!.getStringExtra("defaultText")
            val bundle = Bundle()
            bundle.putInt("noteId", noteId)
            bundle.putInt("status", status)
            bundle.putString("noteTitle", noteTitle)
            bundle.putString("defaultText", defaultText)
            setFragmentByBundle(
                supportFragmentManager,
                PinFragment(),
                true,
                R.id.fmlVerification,
                bundle
            )
        } else {
            val noteId = intent!!.getIntExtra("noteId", 0)
            val bundle = Bundle()
            bundle.putInt("noteId", noteId)
            bundle.putInt("status", status)
            setFragmentByBundle(
                supportFragmentManager,
                PinFragment(),
                true,
                R.id.fmlVerification,
                bundle
            )
        }
    }
}