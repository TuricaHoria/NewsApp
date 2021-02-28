package app.baseappsetup.helpers.extensions

import app.baseappsetup.data.lifecycle.AutoDisposable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.addTo(autoDisposable: AutoDisposable) {
    autoDisposable.add(this)
}

fun Disposable.addTo(disposable: CompositeDisposable) {
    disposable.add(this)
}
