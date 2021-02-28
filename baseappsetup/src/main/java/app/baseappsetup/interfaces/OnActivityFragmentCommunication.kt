package app.baseappsetup.interfaces

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment

interface OnActivityFragmentCommunication {
    fun onAddFragment(
        TAG: String,
        bundle: Bundle? = null,
        animated: Boolean = false,
        currentFragment: Fragment? = null,
        targetFragmentRequestCode: Int? = null,
        addToBackStack: Boolean = true
    )

    fun onReplaceFragment(
        TAG: String,
        bundle: Bundle? = null,
        animated: Boolean = true,
        currentFragment: Fragment? = null,
        targetFragmentRequestCode: Int? = null,
        addToBackStack: Boolean = true
    )

    fun onRemoveFragment(TAG: String, animated: Boolean = true)
    fun onPopFragment()

    fun setActionBarTitle(title: String, isForced: Boolean = false)

    fun setActionBarVisibility(isVisible: Boolean)

    fun requestActionBarAccess(): ActionBar?

    fun removeActionBarTitle()
}