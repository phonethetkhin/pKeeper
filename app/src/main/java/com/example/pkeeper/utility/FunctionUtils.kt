@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.example.pkeeper.utility

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * This function is about getting the current system time.
 *
 * @return
 */
fun getFullDate(): String {
    val c = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return dateFormat.format(c.time)
}

@SuppressLint("SimpleDateFormat")
fun getDateString(
    stringDate: String?,
    stringDateFormat: String?,
    returnDateFormat: String?
): String? {
    val date = SimpleDateFormat(stringDateFormat).parse(stringDate)
    return SimpleDateFormat(returnDateFormat).format(date)
}
