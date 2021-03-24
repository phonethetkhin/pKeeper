package com.ptk.pkeeper.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ptk.pkeeper.R
import com.ptk.pkeeper.interfaces.BiometricsCallback
import com.ptk.pkeeper.utility.showToastShort

class FingerPrintActivity : AppCompatActivity(), BiometricsCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finger_print)
    }

    override fun onSdkVersionNotSupported() {
        showToastShort(this, "Your Android Version cannot use FingerPrint")
    }

    override fun onBiometricAuthenticationNotSupported() {
        showToastShort(this, "Your device does not support FingerPrint")

    }

    override fun onBiometricAuthenticationNotAvailable() {
        showToastShort(this, "At least, register FingerPrint once in system setting")
    }

    override fun onBiometricAuthenticationPermissionNotGranted() {
        showToastShort(this, "Permission needed for Fingerprint")
    }

    override fun onBiometricAuthenticationInternalError(error: String?) {
        showToastShort(this, "Fingerprint Internal Server Error")
    }

    override fun onAuthenticationFailed() {
        showToastShort(this, "Authentication Failed")
    }

    override fun onAuthenticationCancelled() {
        showToastShort(this, "Authentication Cancelled")
    }

    override fun onAuthenticationSuccessful() {
        showToastShort(this, "Authentication Successful")
    }

    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
        showToastShort(this, helpString.toString())
    }

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
        showToastShort(this, errString.toString())
    }
}