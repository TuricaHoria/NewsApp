package app.baseappsetup.data.network.interceptors

import android.content.Context
import app.baseappsetup.R
import app.baseappsetup.data.network.ConnectivityStatus
import app.baseappsetup.data.network.NetworkEventBus
import app.baseappsetup.data.network.NetworkState
import app.baseappsetup.data.network.NetworkErrorMessageCode
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject


class NetworkInterceptor(private val context: Context) : Interceptor {

    private val networkEvent: NetworkEventBus = NetworkEventBus

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        /**
         * Check if there is internet available in the device.
         * If not, pass the networkState as NoInternet.
         * */
        if (!ConnectivityStatus.isConnected(context)) {
            networkEvent.publish(NetworkState.NoInternet)
        } else {
            try {
                var networkState = NetworkState.from(response.code)

                networkState.api = request.url.toString()

                when (response.isSuccessful) {
                    true -> {
                        networkState.message = null
                    }

                    else -> {
                        val responseBodyCopy = response.peekBody(java.lang.Long.MAX_VALUE)
                        val content = responseBodyCopy.string()
                        networkState.message = getErrorMessage(content)
                        networkState.errorMessageCode = getErrorMessageCode(content)
                    }
                }

                networkEvent.publish(networkState)
            } catch (e: Throwable) {
                networkEvent.publish(NetworkState.Unknown)
            }
        }

        return response
    }

    private fun getErrorMessageCode(response: String): NetworkErrorMessageCode? =
        try {
            when {

                JSONObject(response).has(NetworkState.MESSAGE) ->
                    JSONObject(response).getString(NetworkState.MESSAGE)?.let { errorCode ->
                        NetworkErrorMessageCode.from(errorCode)
                    }

                JSONObject(response).has(NetworkState.ERROR_DESCRIPTION) ->
                    JSONObject(response).getString(NetworkState.ERROR_DESCRIPTION)
                        ?.let { errorDescription ->
                            NetworkErrorMessageCode.from(errorDescription)
                        }

                else -> null
            }
        } catch (ex: JSONException) {
           null
        }

    private fun getErrorMessage(response: String): String? {
        try {
            val errorCode = JSONObject(response).getString(NetworkState.MESSAGE)
            val errorCodesList = context.resources.getStringArray(R.array.error_codes)
            val errorMessagesList = context.resources.getStringArray(R.array.error_messages)

            val index = errorCodesList.indexOf(errorCode)
            return when (index >= 0 && index < errorMessagesList.size) {
                true -> errorMessagesList[index]
                false -> errorCode
            }
        } catch (ex: JSONException) {
            return try {
                val errorDescription =
                    JSONObject(response).getString(NetworkState.ERROR_DESCRIPTION)
                val errorCodesList = context.resources.getStringArray(R.array.error_codes)
                val errorMessagesList = context.resources.getStringArray(R.array.error_messages)

                val index = errorCodesList.indexOf(errorDescription)
                return when (index >= 0 && index < errorMessagesList.size) {
                    true -> errorMessagesList[index]
                    false -> null
                }
            } catch (exc: JSONException) {
                null
            }
        }
    }

}