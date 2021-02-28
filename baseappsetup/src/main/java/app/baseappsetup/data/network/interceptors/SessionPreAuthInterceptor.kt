package app.baseappsetup.data.network.interceptors

import android.content.Context
import app.baseappsetup.data.client.APIClient
import okhttp3.Interceptor
import okhttp3.Response

class SessionPreAuthInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain
                .request()
                .newBuilder()
                .addHeader(
                    APIClient.AUTHORIZATION,
                    "${APIClient.BASIC} ${APIClient.BASIC_AUTH_VALUE}"
                )
                .build()
        )
    }
}