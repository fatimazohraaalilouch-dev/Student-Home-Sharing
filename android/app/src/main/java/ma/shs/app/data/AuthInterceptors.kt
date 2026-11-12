package ma.shs.app.data

import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider

class BearerInterceptor @Inject constructor(private val tokens: TokenStore) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val token = tokens.accessToken
        val withAuth = if (token != null && !req.url.encodedPath.contains("/auth/"))
            req.newBuilder().header("Authorization", "Bearer $token").build() else req
        return chain.proceed(withAuth)
    }
}

/** Sur 401, tente un refresh. Si le refresh échoue, vide les jetons : l'UI affiche « Session expirée ». */
class TokenAuthenticator @Inject constructor(
    private val tokens: TokenStore,
    private val authApi: Provider<AuthApi>
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.request.url.encodedPath.contains("/auth/")) return null
        val refresh = tokens.refreshToken ?: return null
        val result = runCatching { authApi.get().refresh(RefreshRequest(refresh)).execute() }.getOrNull()
        val body = result?.body()
        if (result == null || !result.isSuccessful || body == null) {
            tokens.clear()
            return null
        }
        tokens.accessToken = body.accessToken
        tokens.refreshToken = body.refreshToken
        return response.request.newBuilder().header("Authorization", "Bearer ${body.accessToken}").build()
    }
}
