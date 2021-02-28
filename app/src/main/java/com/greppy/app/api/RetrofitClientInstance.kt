package com.greppy.app.api

import app.baseappsetup.data.client.APIClient
import app.baseappsetup.data.network.authenticators.SessionAuthenticator
import app.baseappsetup.data.network.interceptors.SessionAuthInterceptor
import app.baseappsetup.data.network.interceptors.SessionPreAuthInterceptor
import com.greppy.app.ApplicationController
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Boolean
import java.util.concurrent.TimeUnit

object RetrofitClientInstance: APIClient() {

    private val preAuthInterceptor by lazy {
        SessionPreAuthInterceptor(context = ApplicationController.instance)
    }

    private val authInterceptor by lazy {
        SessionAuthInterceptor(context = ApplicationController.instance)
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
            .followRedirects(Boolean.FALSE)
            .followSslRedirects(Boolean.FALSE)
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