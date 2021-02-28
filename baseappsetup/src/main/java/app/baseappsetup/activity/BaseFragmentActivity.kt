package app.baseappsetup.activity

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import app.baseappsetup.R
import app.baseappsetup.helpers.extensions.logErrorMessage
import app.baseappsetup.helpers.extensions.showCustomAlert
import app.baseappsetup.helpers.utils.ifPairLet
import app.baseappsetup.interfaces.OnActivityAlert
import app.baseappsetup.interfaces.OnActivityFragmentCommunication
import app.baseappsetup.interfaces.OnRequestAction
import app.baseappsetup.widget.LoadingDialog

abstract class BaseFragmentActivity : AppCompatActivity(), OnActivityAlert,
    OnActivityFragmentCommunication {

    private var mProgressDialog: LoadingDialog? = null
    protected var mFragmentManager: FragmentManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
        setupViews()

        mFragmentManager = this.supportFragmentManager
    }

    override fun onBackPressed() {
        val fragmentsCount = mFragmentManager?.backStackEntryCount ?: 0

        if (fragmentsCount <= 1) {
            finish()
            return
        }

        super.onBackPressed()
    }

    /**
     * Function used to initialize the activity_welcome's views
     */
    private fun initViews() {
        mProgressDialog = LoadingDialog(this)
    }

    private fun setupViews() {
        val pd = mProgressDialog
        if (pd != null) {
            pd.requestWindowFeature(Window.FEATURE_NO_TITLE)
            pd.setContentView(R.layout.item_alert_progress)
            pd.setCancelable(false)

            pd.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    /*************************************************************
     * Mark: ALERT
     *************************************************************/

    /**
     * Function called to show the progress spinner - [.mProgressDialog]
     */
    override fun showProgressDialog() {
        val pd = mProgressDialog
        if (pd != null && !pd.isShowing) {
            runOnUiThread { pd.show() }
        }
    }

    /**
     * Function called to hide the progress spinner - [.mProgressDialog]
     */
    override fun hideProgressDialog() {
        val pd = mProgressDialog
        if (pd != null && pd.isShowing) {
            runOnUiThread { pd.dismiss() }
        }
    }

    override fun showAlert(
        message: String,
        isPositiveButton: Boolean,
        isNegativeButton: Boolean,
        positiveButtonText: String,
        negativeButtonText: String,
        actionListener: OnRequestAction?,
        isCancelable: Boolean
    ) = this.showCustomAlert(
        message = message,
        isPositiveButton = isPositiveButton,
        isNegativeButton = isNegativeButton,
        positiveButtonText = positiveButtonText,
        negativeButtonText = negativeButtonText,
        actionListener = actionListener,
        isCancelable = isCancelable
    )

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /*************************************************************
     * Mark: OnActivityFragmentCommunication
     */
    private enum class FragmentActions {
        ADD,
        REPLACE,
        REMOVE
    }

    override fun onAddFragment(
        TAG: String,
        bundle: Bundle?,
        animated: Boolean,
        currentFragment: Fragment?,
        targetFragmentRequestCode: Int?,
        addToBackStack: Boolean
    ) {
        onCreateFragmentAction(
            TAG,
            FragmentActions.ADD,
            bundle,
            animated,
            currentFragment,
            targetFragmentRequestCode,
            addToBackStack = addToBackStack
        )
    }

    override fun onReplaceFragment(
        TAG: String,
        bundle: Bundle?,
        animated: Boolean,
        currentFragment: Fragment?,
        targetFragmentRequestCode: Int?,
        addToBackStack: Boolean
    ) {
        onCreateFragmentAction(
            TAG = TAG,
            fragmentAction = FragmentActions.REPLACE,
            bundle = bundle,
            animated = animated,
            currentFragment = currentFragment,
            targetFragmentRequestCode = targetFragmentRequestCode,
            addToBackStack = addToBackStack
        )
    }

    override fun onRemoveFragment(TAG: String, animated: Boolean) {
        onCreateFragmentAction(TAG, FragmentActions.REMOVE, animated = animated)
    }

    override fun onPopFragment() {
        getContainerFragmentManager()?.popBackStack()
    }

    override fun setActionBarTitle(title: String, forceVisibility: Boolean) {
        if (forceVisibility) {
            supportActionBar?.setDisplayShowTitleEnabled(true)
        }
        supportActionBar?.title = title
    }

    override fun removeActionBarTitle() {
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun requestActionBarAccess(): ActionBar? = supportActionBar

    override fun setActionBarVisibility(isVisible: Boolean) {
        when (isVisible) {
            true -> supportActionBar?.show()
            false -> supportActionBar?.hide()
        }
    }

    /*************************************************************
     * Mark: Fragment actions
     *************************************************************/

    protected abstract fun getFragmentContainer(): Int?
    protected abstract fun getContainerFragmentManager(): FragmentManager?

    private fun onAddFragment(
        currentFragment: Fragment?,
        fragment: Fragment,
        TAG: String,
        animated: Boolean,
        addToBackStack: Boolean
    ) {
        val fm = getContainerFragmentManager() ?: return
        val fragmentContainer = getFragmentContainer() ?: return

        val transaction = fm.beginTransaction()

        if (animated) {
            transaction.setCustomAnimations(
                getFragmentTransitionAnimRightToLeft()[0],
                getFragmentTransitionAnimRightToLeft()[1],
                getFragmentTransitionAnimLeftToRight()[0],
                getFragmentTransitionAnimLeftToRight()[1]
            )
        }

        currentFragment?.let {
            transaction.hide(currentFragment)
        }

        val addTransaction = transaction.add(
            fragmentContainer,
            fragment,
            TAG
        )

        when (addToBackStack) {
            true -> addTransaction.addToBackStack(TAG)
        }
        addTransaction.commit()
    }

    private fun onReplaceFragment(
        fragment: Fragment,
        TAG: String,
        animated: Boolean,
        addToBackStack: Boolean
    ) {
        val fm = getContainerFragmentManager() ?: return
        val fragmentContainer = getFragmentContainer() ?: return

        val transaction = fm.beginTransaction()
        if (animated) {
            transaction.setCustomAnimations(
                getFragmentTransitionAnimRightToLeft()[0],
                getFragmentTransitionAnimRightToLeft()[1],
                getFragmentTransitionAnimLeftToRight()[0],
                getFragmentTransitionAnimLeftToRight()[1]
            )
        }

        val replaceTransaction = transaction.replace(
            fragmentContainer,
            fragment,
            TAG
        )

        when (addToBackStack) {
            true -> replaceTransaction.addToBackStack(TAG)
        }
        replaceTransaction.commit()
    }

    private fun onRemoveFragment(fragment: Fragment, animated: Boolean) {
//        val fm = mFragmentManager ?: return
//
//        val mFragmentTransaction =
//            fm.beginTransaction().remove(fragment)
//
//        mFragmentTransaction.commit()
        "Fragment to remove: ${fragment.javaClass.name}, animated = $animated".logErrorMessage()
        getContainerFragmentManager()?.popBackStack()
    }

    private fun onCreateFragmentAction(
        TAG: String,
        fragmentAction: FragmentActions,
        bundle: Bundle? = null,
        animated: Boolean,
        currentFragment: Fragment? = null,
        targetFragmentRequestCode: Int? = null,
        addToBackStack: Boolean = true
    ) {
        val fragment = getFragmentByTag(TAG) ?: return
        fragment.arguments = bundle

        ifPairLet(
            currentFragment,
            targetFragmentRequestCode
        ) { targetFragment, targetFragmentCode ->
            fragment.setTargetFragment(
                targetFragment,
                targetFragmentCode
            )
        }

        when (fragmentAction) {
            FragmentActions.ADD -> onAddFragment(
                currentFragment,
                fragment,
                TAG,
                animated,
                addToBackStack
            )
            FragmentActions.REPLACE -> onReplaceFragment(fragment, TAG, animated, addToBackStack)
            FragmentActions.REMOVE -> onRemoveFragment(fragment, animated)
        }
    }

    protected abstract fun getFragmentByTag(TAG: String): Fragment?

    private fun getFragmentTransitionAnimRightToLeft(): IntArray =
        intArrayOf(R.anim.enter_from_right, R.anim.exit_to_left)

    private fun getFragmentTransitionAnimLeftToRight(): IntArray =
        intArrayOf(R.anim.enter_from_left, R.anim.exit_to_right)

    /*************************************************************
     * Mark: Keyboard
     *************************************************************/

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // check if no view has focus
        currentFocus?.let {
            inputManager.hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}