package com.ptk.pkeeper.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.listener.PatternLockViewListener
import com.andrognito.patternlockview.utils.PatternLockUtils
import com.ptk.pkeeper.R
import com.ptk.pkeeper.utility.showToastShort
import kotlinx.android.synthetic.main.activity_pattern.*


class PatternActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pattern)
        plvPatternLockView.addPatternLockListener(object : PatternLockViewListener {
            override fun onStarted() {}

            override fun onProgress(progressPattern: MutableList<PatternLockView.Dot>?) {}

            override fun onComplete(pattern: MutableList<PatternLockView.Dot>?) {
                Log.d("pattern", PatternLockUtils.patternToString(plvPatternLockView, pattern))
                if (PatternLockUtils.patternToString(plvPatternLockView, pattern).equals("012")) {
                    plvPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT)
                    showToastShort(this@PatternActivity, "Correct")
                } else {
                    plvPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG)
                    showToastShort(this@PatternActivity, "Wrong Pin")
                }
            }

            override fun onCleared() {}

        })
    }
}