package app.baseappsetup.data.api

import app.baseappsetup.data.models.Session
import retrofit2.Call
import retrofit2.http.*

interface AuthorizationAPI {

    companion object {
        const val GRANT_TYPE = "grant_type"
        const val GRANT_TYPE_REFRESH_TOKEN = "refresh_token"
        const val SCOPE = "scope"
        const val SCOPE_MOBILE = "mobile"
        const val REFRESH_TOKEN = "refresh_token"
    }

   // @POST("${BuildConfig.AUTH_SERVICE_SESSION}oauth/token") TODO uncomment and setup for refresh token
    @POST("")
    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    fun refreshToken(@Field(GRANT_TYPE) grantType: String,
                     @Field(SCOPE) scope: String,
                     @Field(REFRESH_TOKEN) refreshToken: String): Call<Session>

}