package app.baseappsetup.helpers.extensions

import android.content.Context
import app.baseappsetup.data.models.Session
import app.baseappsetup.helpers.UtilsSharedPreferences

fun Context.saveSession(session: Session) {
    UtilsSharedPreferences.saveSessionToken(this, session)
}

fun Context.removeSession() {
    UtilsSharedPreferences.removeSessionToken(this)
}