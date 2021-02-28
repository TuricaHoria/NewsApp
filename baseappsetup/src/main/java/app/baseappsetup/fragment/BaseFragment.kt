package app.baseappsetup.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import app.baseappsetup.R
import app.baseappsetup.data.lifecycle.AutoDisposable
import app.baseappsetup.helpers.utils.KeyboardEventListener
import app.baseappsetup.interfaces.OnActivityAlert
import app.baseappsetup.interfaces.OnActivityFragmentCommunication

abstract class BaseFragment : Fragment() {

    protected var mAlertCallback: OnActivityAlert? = null
    private var mHandleFragmentsCallback: OnActivityFragmentCommunication? = null

    protected val autoDisposable by lazy {
        AutoDisposable()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnActivityFragmentCommunication) {
            mHandleFragmentsCallback = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }

        if (context is OnActivityAlert) {
            mAlertCallback = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        mHandleFragmentsCallback = null
        mAlertCallback = null
    }

    override fun onStart() {
        super.onStart()

        autoDisposable.bindTo(this.lifecycle)
    }

    override fun onStop() {
        super.onStop()
        hideProgressDialog()
    }

    /**
     * Handle fragments by tag
     */
    protected fun onAddFragmentByTAG(
        TAG: String,
        bundle: Bundle? = null,
        animated: Boolean = false,
        currentFragment: Fragment? = null,
        targetFragmentRequestCode: Int? = null,
        addToBackStack: Boolean = true
    ) =
        mHandleFragmentsCallback?.onAddFragment(
            TAG,
            bundle,
            animated,
            currentFragment,
            targetFragmentRequestCode,
            addToBackStack
        )

    protected fun onReplaceFragmentByTAG(
        TAG: String,
        bundle: Bundle? = null,
        animated: Boolean = true,
        currentFragment: Fragment? = null,
        targetFragmentRequestCode: Int? = null,
        addToBackStack: Boolean = true
    ) =
        mHandleFragmentsCallback?.onReplaceFragment(
            TAG,
            bundle,
            animated,
            currentFragment,
            targetFragmentRequestCode,
            addToBackStack
        )

    protected fun onRemoveFragmentByTAG(TAG: String) =
        mHandleFragmentsCallback?.onRemoveFragment(TAG)

    protected fun onPopFragment() =
        mHandleFragmentsCallback?.onPopFragment()

    protected fun setActionBarTitle(
        title: String,
        isForced: Boolean = false,
        ignoreFragmentVisibility: Boolean = false
    ) {
        if (!ignoreFragmentVisibility) {
            when (isVisible) {
                true -> mHandleFragmentsCallback?.setActionBarTitle(title, isForced)
                else -> return
            }
        } else {
            mHandleFragmentsCallback?.setActionBarTitle(title, isForced)
        }
    }

    protected fun removeActionBarTitle() =
        mHandleFragmentsCallback?.removeActionBarTitle()

    protected fun requestActionBar() =
        mHandleFragmentsCallback?.requestActionBarAccess()

    protected fun setActionBarVisibility(isVisible: Boolean) =
        mHandleFragmentsCallback?.setActionBarVisibility(isVisible)

    /**
     * Show alerts
     */
    protected fun showProgressDialog() =
        mAlertCallback?.showProgressDialog()

    protected fun hideProgressDialog() =
        mAlertCallback?.hideProgressDialog()

    protected fun showInternetAlert() = mAlertCallback?.showAlert(
        message = getString(R.string.alert_message_no_internet),
        isNegativeButton = false
    )

    /**
     * Hide keyboard
     */
    fun hideKeyboardFrom(view: View) {
        val ctx = context ?: return
        val imm = ctx.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun hideKeyboard() {
        val inputManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // check if no view has focus
        activity?.currentFocus?.let {
            inputManager.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    protected fun registerKeyboardListener(consumeKeyboardVisibility: (Boolean) -> Unit) =
        activity?.run {
            KeyboardEventListener(this) { isOpen ->
                consumeKeyboardVisibility.invoke(isOpen)
            }
        }
}