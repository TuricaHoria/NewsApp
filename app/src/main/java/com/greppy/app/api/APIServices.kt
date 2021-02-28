package com.greppy.app.api

import com.greppy.app.models.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query
import io.reactivex.Observable


interface APIServices {

    companion object {
        const val API_KEY = "apiKey"
        const val PAGE = "page"
        const val COUNTRY = "country"
    }

    @GET("top-headlines")
    fun getNews(
        @Query(API_KEY) apiKey: String,
        @Query(PAGE) page: Int,
        @Query(COUNTRY) country : String
    ): Observable<NewsResponse>
}