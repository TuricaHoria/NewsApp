package com.greppy.app.utils

import java.text.SimpleDateFormat
import java.util.*


fun String.toDate(): Date? {
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)

    if (this.isNotEmpty()) {
        return simpleDateFormat.parse(this)
    }
    return null
}

fun Date.untilNow(secondDate: Date): String {

    val diff: Long = secondDate.time - this.time
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return if (days > 0) {
        "$days D"
    } else {
        if (hours > 0) {
            "$hours H"
        } else {
            if (minutes > 0) {
                "$minutes m"
            } else {
                "$seconds s"
            }
        }
    }
}