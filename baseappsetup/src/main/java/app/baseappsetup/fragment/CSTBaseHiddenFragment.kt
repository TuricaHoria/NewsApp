package app.baseappsetup.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.*

abstract class CSTBaseHiddenFragment : Fragment() {

    //  LIFECYCLE OWNERS
    private val _hideableLifecycleOwner = HideableLifecycleOwner()
    val hideableLifecycle: LifecycleOwner get() = _hideableLifecycleOwner
//    private val _hideableViewLifecycleOwner = HideableViewLifecycleOwner()
//    val hideableViewLifecycle: LifecycleOwner get() = _hideableViewLifecycleOwner

    //  HIDDEN
    private var onHiddenOrParentHiddenChangedCalled = true
    private val isHiddenOrParentHidden: Boolean
        get() = isHidden || ((parentFragment as CSTBaseHiddenFragment?)?.isHiddenOrParentHidden == true)
    private val _isHiddenForChildren = MutableLiveData(false)
    val isHiddenForChildren: LiveData<Boolean> = _isHiddenForChildren.distinctUntilChanged()

    // ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===
    // LIFECYCLE
    // ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  HIDDEN
        /* start listening to hide state changes of your direct parent(if exists) */
        (parentFragment as CSTBaseHiddenFragment?)?.isHiddenForChildren?.observe(this, Observer {
            invokeOnHiddenOrParentHiddenChanged()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //  LIFECYCLE OWNERS
//        _hideableViewLifecycleOwner.initialize()
    }

    override fun onDestroyView() {
        //  LIFECYCLE OWNERS
//        _hideableViewLifecycleOwner.deinitialize()
        super.onDestroyView()
    }

    override fun onDestroy() {
        //  HIDDEN
        (parentFragment as CSTBaseHiddenFragment?)?.isHiddenForChildren?.removeObservers(this)
        super.onDestroy()
    }

    // ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===
    // HIDDEN
    // ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===

    final override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        invokeOnHiddenOrParentHiddenChanged()
    }

    private fun invokeOnHiddenOrParentHiddenChanged() {
        onHiddenOrParentHiddenChangedCalled = false
        onHiddenOrParentHiddenChanged(isHiddenOrParentHidden)
        if (!onHiddenOrParentHiddenChangedCalled)
            throw IllegalStateException("super.onHiddenOrParentHiddenChanged() not called!")
    }

    protected open fun onHiddenOrParentHiddenChanged(hidden: Boolean) {
        _hideableLifecycleOwner.onHiddenChanged(
            hidden = hidden,
            hiddenFragmentPaused = { hiddenFragmentPaused() },
            hiddenFragmentResumed = { hiddenFragmentResumed() }
        )
//        _hideableViewLifecycleOwner.onHiddenChanged(hidden)
        _isHiddenForChildren.value = hidden
        onHiddenOrParentHiddenChangedCalled = true
    }

    private fun hiddenFragmentPaused() {
        onPause()
    }

    private fun hiddenFragmentResumed() {
        onResume()
    }

    // ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===
    // LIFECYCLE OWNERS
    // ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===== ===

    private inner class HideableLifecycleOwner : LifecycleOwner, LifecycleEventObserver {
        private val lifecycleRegistry = LifecycleRegistry(this)

        init {
            this@CSTBaseHiddenFragment.lifecycle.addObserver(this)
        }

        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            when (event) {
                Lifecycle.Event.ON_START, Lifecycle.Event.ON_RESUME,
                Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_STOP ->
                    lifecycleRegistry.handleLifecycleEvent(if (this@CSTBaseHiddenFragment.isHiddenOrParentHidden) Lifecycle.Event.ON_STOP else event)
                else -> lifecycleRegistry.handleLifecycleEvent(event)
            }
        }

        internal fun onHiddenChanged(
            hidden: Boolean,
            hiddenFragmentPaused: () -> Unit,
            hiddenFragmentResumed: () -> Unit
        ) {
            when {
                this@CSTBaseHiddenFragment.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED) && hidden -> {
                    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
                    hiddenFragmentPaused.invoke()
                }
                this@CSTBaseHiddenFragment.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED) -> {
                    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
                    hiddenFragmentResumed.invoke()
                }
                this@CSTBaseHiddenFragment.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED) ->
                    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
                this@CSTBaseHiddenFragment.lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED) ->
                    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
            }
        }

        override fun getLifecycle(): Lifecycle = lifecycleRegistry
    }

//    private inner class HideableViewLifecycleOwner : LifecycleOwner, LifecycleEventObserver {
//        private var isInitialized = false
//        private val lifecycleRegistry = LifecycleRegistry(this)
//
//        fun initialize() {
//            this@CSTBaseHiddenFragment.viewLifecycleOwner.lifecycle.addObserver(this)
//            isInitialized = true
//        }
//
//        fun deinitialize() {
//            isInitialized = false
//            this@CSTBaseHiddenFragment.viewLifecycleOwner.lifecycle.removeObserver(this)
//            lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
//        }
//
//        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
//            when (event) {
//                Lifecycle.Event.ON_START, Lifecycle.Event.ON_RESUME,
//                Lifecycle.Event.ON_PAUSE, Lifecycle.Event.ON_STOP ->
//                    lifecycleRegistry.handleLifecycleEvent(
//                        if (this@CSTBaseHiddenFragment.isHiddenOrParentHidden) Lifecycle.Event.ON_STOP else event
//                    )
//                else -> lifecycleRegistry.handleLifecycleEvent(event)
//            }
//        }
//
//        internal fun onHiddenChanged(hidden: Boolean) {
//            if (!isInitialized) return
//            when {
//                this@CSTBaseHiddenFragment.viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED) && hidden ->
//                    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
//                this@CSTBaseHiddenFragment.viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED) ->
//                    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
//                this@CSTBaseHiddenFragment.viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED) ->
//                    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
//                this@CSTBaseHiddenFragment.viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED) ->
//                    lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
//            }
//        }
//
//        override fun getLifecycle(): Lifecycle = lifecycleRegistry
//    }
}