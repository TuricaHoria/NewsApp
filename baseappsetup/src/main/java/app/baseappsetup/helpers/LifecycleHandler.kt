package app.baseappsetup.helpers

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import app.baseappsetup.helpers.extensions.logErrorMessage

class LifecycleHandler : ActivityLifecycleCallbacks {
    private var created: Int = 0

    fun getAliveActivitiesCounter() = created

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        ++created
        "Alive Activities (alive): $created".logErrorMessage()
    }

    override fun onActivityDestroyed(activity: Activity) {
        --created
        "Alive Activities (destroy): $created".logErrorMessage()
    }

    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
}