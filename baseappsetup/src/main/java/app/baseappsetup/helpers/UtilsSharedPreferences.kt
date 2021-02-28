package app.baseappsetup.helpers

import android.content.Context
import app.baseappsetup.data.models.Session

object UtilsSharedPreferences {

    private const val SESSION_TOKEN_KEY = "sessionToken"
    private const val REFRESH_TOKEN_KEY = "refreshToken"

    fun saveSessionToken(context: Context, session: Session) {
        val editor = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).edit()

        editor.putString(SESSION_TOKEN_KEY, session.accessToken)
        //editor.putString(REFRESH_TOKEN_KEY, session.refreshToken)
        editor.apply()
    }

    fun getSessionToken(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        return sharedPreferences.getString(SESSION_TOKEN_KEY, "") ?: ""
    }

    fun getRefreshToken(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        return sharedPreferences.getString(REFRESH_TOKEN_KEY, "") ?: ""
    }

    fun removeSessionToken(context: Context) {
        val settings = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        settings.edit().remove(SESSION_TOKEN_KEY).apply()
        settings.edit().remove(REFRESH_TOKEN_KEY).apply()
    }
}