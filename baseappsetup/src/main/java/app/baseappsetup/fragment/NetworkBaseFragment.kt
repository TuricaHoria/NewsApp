package app.baseappsetup.fragment

import app.baseappsetup.data.network.NetworkEventBus

abstract class NetworkBaseFragment : BaseFragment() {

    override fun onResume() {
        super.onResume()

        if(isHidden)
            return

        registerNetworkEventBus()
    }

    override fun onPause() {
        super.onPause()

        NetworkEventBus.unregister(this)
    }

    protected abstract fun registerNetworkEventBus()
}