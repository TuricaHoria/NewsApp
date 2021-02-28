package app.baseappsetup

import android.app.Application
import app.baseappsetup.helpers.LifecycleHandler

abstract class MApplicationController : Application() {

    companion object {
        lateinit var instance: MApplicationController
    }

    val lifecycleHandler by lazy {
        LifecycleHandler()
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        registerActivityLifecycleCallbacks(lifecycleHandler)
    }

}
