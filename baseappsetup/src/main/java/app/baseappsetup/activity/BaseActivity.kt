package app.baseappsetup.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.baseappsetup.R
import app.baseappsetup.data.lifecycle.AutoDisposable
import app.baseappsetup.data.network.NetworkEventBus
import app.baseappsetup.helpers.extensions.showCustomAlert
import app.baseappsetup.interfaces.OnActivityAlert
import app.baseappsetup.interfaces.OnRequestAction
import app.baseappsetup.widget.LoadingDialog

abstract class BaseActivity : AppCompatActivity(), OnActivityAlert {

    private var mProgressDialog: LoadingDialog? = null

    protected val autoDisposable by lazy {
        AutoDisposable()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
        setupViews()
    }

    override fun onStart() {
        super.onStart()

        autoDisposable.bindTo(this.lifecycle)
    }

    override fun onPause() {
        super.onPause()

        NetworkEventBus.unregister(this)
    }

    override fun onResume() {
        super.onResume()

        registerNetworkEventBus()
    }

    /**
     * Handle network errors
     */
    protected abstract fun registerNetworkEventBus()

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

}