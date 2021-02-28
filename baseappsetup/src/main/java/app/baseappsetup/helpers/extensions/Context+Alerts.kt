package app.baseappsetup.helpers.extensions

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import app.baseappsetup.BuildConfig
import app.baseappsetup.R
import app.baseappsetup.interfaces.OnRequestAction


fun Context.showDebugToast(message: String) {
    if (BuildConfig.DEBUG) {
        message.logErrorMessage()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

fun Context.showToastWithMessage(message: String) {
    message.logErrorMessage()
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showCustomAlert(
    message: String,
    isPositiveButton: Boolean = true,
    isNegativeButton: Boolean = true,
    positiveButtonText: String = getString(R.string.alert_ok),
    negativeButtonText: String = getString(R.string.alert_cancel),
    actionListener: OnRequestAction? = null,
    isCancelable: Boolean = false
): AlertDialog? {
    val b = AlertDialog.Builder(this)
        .setMessage(message)
        .setCancelable(isCancelable)

    var positiveText = getString(R.string.alert_ok)
    actionListener?.let {
        if (isNegativeButton) {
            b?.setNegativeButton(negativeButtonText) { _, _ ->
                it.requestNegativeAction()
            }
        }

        positiveText = positiveButtonText
    }

    if (isPositiveButton) {
        b?.setPositiveButton(positiveText) { _, _ ->
            actionListener?.requestPositiveAction()
        }
    }

    val mAlert = b?.create() ?: return null
    mAlert.show()

    return mAlert
}