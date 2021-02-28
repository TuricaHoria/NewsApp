package app.baseappsetup.data.client

import app.baseappsetup.MApplicationController
import app.baseappsetup.data.network.interceptors.SessionAuthInterceptor
import app.baseappsetup.data.network.authenticators.SessionAuthenticator
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object SessionAPIClient: APIClient() {

    private val authInterceptor by lazy {
        SessionAuthInterceptor(context = MApplicationController.instance)
    }

    private val authenticator by lazy {
        SessionAuthenticator
    }

    val retrofitClient: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(getBaseApi())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(networkInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(authenticator)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

}