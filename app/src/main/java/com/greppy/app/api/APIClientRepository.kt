package com.greppy.app.api

import com.greppy.app.models.NewsResponse
import io.reactivex.Observable

object APIClientRepository {

    private val authenticationAPI: APIServices by lazy {
        RetrofitClientInstance.retrofitClient.create(APIServices::class.java)
    }

    fun getNews(key: String, pageNumber: Int, country: String): Observable<NewsResponse> {
        return authenticationAPI.getNews(key, pageNumber, country)
    }
}