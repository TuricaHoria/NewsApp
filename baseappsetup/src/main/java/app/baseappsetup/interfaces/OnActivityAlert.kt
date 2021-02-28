package app.baseappsetup.interfaces

import androidx.appcompat.app.AlertDialog
import app.baseappsetup.MApplicationController
import app.baseappsetup.R

interface OnActivityAlert {
    fun showProgressDialog()
    fun hideProgressDialog()

    fun showToast(message: String)

    fun showAlert(
            message: String,
            isPositiveButton: Boolean = true,
            isNegativeButton: Boolean = true,
            positiveButtonText: String = MApplicationController.instance.getString(R.string.alert_ok),
            negativeButtonText: String = MApplicationController.instance.getString(R.string.alert_cancel),
            actionListener: OnRequestAction? = null,
            isCancelable: Boolean = false
    ): AlertDialog?
}