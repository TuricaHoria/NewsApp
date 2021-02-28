package app.baseappsetup.data.network.authenticators

import app.baseappsetup.MApplicationController
import app.baseappsetup.data.client.APIClient
import app.baseappsetup.data.repository.AuthorizationAPIRepository
import app.baseappsetup.helpers.UtilsSharedPreferences
import app.baseappsetup.helpers.extensions.logErrorMessage
import app.baseappsetup.helpers.extensions.saveSession
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

object SessionAuthenticator: Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // We need to have a token in order to refresh it.
        val token = UtilsSharedPreferences.getSessionToken(MApplicationController.instance)
        if(token.isEmpty()) return null

        synchronized(this) {
            val newToken = UtilsSharedPreferences.getSessionToken(MApplicationController.instance)

            "New Token: $newToken\nOld Token: $token".logErrorMessage()

            // Check if the request made was previously made as an authenticated request.
            if (response.request.header(APIClient.AUTHORIZATION) != null) {

                // If the token has changed since the request was made, use the new token.
                if (newToken != token) {
                    return response.request
                        .newBuilder()
                        .removeHeader(APIClient.AUTHORIZATION)
                        .addHeader(APIClient.AUTHORIZATION, "${APIClient.BEARER} $newToken")
                        .build()
                }

                "Prepare to refresh access token.".logErrorMessage()

                val refreshToken = UtilsSharedPreferences.getRefreshToken(MApplicationController.instance)
                val call = AuthorizationAPIRepository.refreshToken(refreshToken = refreshToken)
                val responseCall = call.execute()
                val sessionResult = responseCall.body()

                "Refresh token finished. Verify the result...".logErrorMessage()

                if (responseCall.code() != 200) {
                    "Failed to refresh access token. Force log out.".logErrorMessage()
                    return null
                }

                val accessToken = sessionResult?.accessToken ?: run {
                    "Failed to get the new access token. Force log out".logErrorMessage()
                    return null
                }

                sessionResult?.let {
                    "Access token has been refreshed".logErrorMessage()
                    //sessionResult.refreshToken.logErrorMessage()
                    sessionResult.accessToken.logErrorMessage()

                    MApplicationController.instance.saveSession(sessionResult)
                }

                // Retry the request with the new token.
                return response.request
                    .newBuilder()
                    .removeHeader(APIClient.AUTHORIZATION)
                    .addHeader(APIClient.AUTHORIZATION, "${APIClient.BEARER} $accessToken")
                    .build()
            }
        }

        return null
    }
}