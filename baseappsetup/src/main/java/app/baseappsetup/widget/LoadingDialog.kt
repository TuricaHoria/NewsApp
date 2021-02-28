package app.baseappsetup.widget

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import androidx.annotation.StyleRes

class LoadingDialog  : Dialog {

    private var mContext: Context

    constructor(context: Context) :
            super(context) {
        this.mContext = context
    }

    constructor(context: Context,
                @StyleRes themeResId: Int) :
            super(context, themeResId) {

        this.mContext = context
    }

    constructor(context: Context,
                cancelable: Boolean,
                cancelListener: DialogInterface.OnCancelListener?) :
            super(context, cancelable, cancelListener) {

        this.mContext = context
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val c = mContext
        if (c is Activity) {
            c.onBackPressed()
        }
    }
}