package app.baseappsetup.interfaces

interface OnRequestDoneWithResult {
    fun onRequestSuccess(result: Any)
    fun onRequestFailed(errorMessage: String, unauthorized: Boolean = false)
}