package app.baseappsetup.helpers

import android.content.Context


object UtilsPermissions {

    fun savePermissionPreference(context: Context, permission: String) {
        val editor = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).edit()
        editor.putBoolean(permission, true)
        editor.apply()
    }

    fun getPermissionPreference(context: Context?, permission: String): Boolean? {
        val ctx = context ?: return null
        val sharedPreferences = ctx.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(permission, false)
    }
}