package app.baseappsetup.data.network.interceptors

import android.content.Context
import app.baseappsetup.data.client.APIClient
import app.baseappsetup.helpers.UtilsSharedPreferences
import okhttp3.Interceptor
import okhttp3.Response

class SessionAuthInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val authToken = UtilsSharedPreferences.getSessionToken(context)

        val request = chain
            .request()
            .newBuilder()

        when (authToken.isEmpty()) {
            false -> request.addHeader(APIClient.AUTHORIZATION, "${APIClient.BEARER} $authToken")
        }

        return chain.proceed(
            request.build()
        )

    }
}