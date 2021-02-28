package app.baseappsetup.data.repository

import app.baseappsetup.data.api.AuthorizationAPI
import app.baseappsetup.data.client.RefreshAuthAPIClient

object AuthorizationAPIRepository {

    private val AUTH_API: AuthorizationAPI by lazy {
        RefreshAuthAPIClient.retrofitClient.create(AuthorizationAPI::class.java)
    }

    fun refreshToken(refreshToken: String) =
        AUTH_API.refreshToken(
            refreshToken = refreshToken,
            grantType = AuthorizationAPI.GRANT_TYPE_REFRESH_TOKEN,
            scope = AuthorizationAPI.SCOPE_MOBILE
        )

}