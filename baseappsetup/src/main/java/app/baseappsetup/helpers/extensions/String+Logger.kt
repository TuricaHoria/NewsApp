package app.baseappsetup.helpers.extensions

import android.util.Log
import app.baseappsetup.BuildConfig
import app.baseappsetup.MApplicationController

fun String.logErrorMessage(TAG: String = MApplicationController.instance.packageName) {
    when (BuildConfig.DEBUG) {
        true -> Log.e(TAG, this)
    }
}
