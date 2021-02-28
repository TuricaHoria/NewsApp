package app.baseappsetup.helpers.extensions

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import app.baseappsetup.helpers.UtilsPermissions.getPermissionPreference
import app.baseappsetup.helpers.UtilsPermissions.savePermissionPreference

fun Activity.requestPermissionFromUser(
    requestCode: Int,
    permissions: Array<String>,
    handlePermissionGranted: (() -> Unit),
    showInfoAlert: ((requestCode: Int, canAskAgain: Boolean) -> Unit)
) {

    var firstDeniedPermission: Pair<String, Boolean>? = null
    val hasPermissions = permissions.all { permission ->
        val isGranted =
            ActivityCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED

        if (!isGranted) {
            val isRationaleMessageNeeds =
                ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
            firstDeniedPermission = Pair(permission, isRationaleMessageNeeds)
        }

        isGranted
    }

    if (!hasPermissions) {
        firstDeniedPermission?.let {
            val currShouldShowStatus = it.second
            val prevShouldShowStatus = getPermissionPreference(this, it.first)

            when (prevShouldShowStatus != currShouldShowStatus) {
                true -> showInfoAlert.invoke(requestCode, it.second)
                else -> {
                    ActivityCompat.requestPermissions(
                        this,
                        permissions,
                        requestCode
                    )
                    savePermissionPreference(this, it.first)
                }
            }
        } ?: run {
            ActivityCompat.requestPermissions(
                this,
                permissions,
                requestCode
            )
        }
    } else {
        handlePermissionGranted.invoke()
    }
}