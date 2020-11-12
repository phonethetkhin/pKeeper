package com.ptk.pkeeper.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ptk.pkeeper.R
import com.ptk.pkeeper.fragment.PinFragment
import com.ptk.pkeeper.utility.setFragmentByBundle

class VerificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        val noteId = intent!!.getIntExtra("noteId", 0)
        val defaultText = intent!!.getStringExtra("defaultText")
        val bundle = Bundle()
        bundle.putInt("noteId", noteId)
        bundle.putString("defaultText", defaultText)
        setFragmentByBundle(
            supportFragmentManager,
            PinFragment(),
            false,
            R.id.fmlVerification,
            bundle
        )
    }
}