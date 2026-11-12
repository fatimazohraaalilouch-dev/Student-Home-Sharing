package ma.shs.app.data

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(val email: String, val password: String)
data class RefreshRequest(val refreshToken: String)
data class AuthTokens(val accessToken: String, val refreshToken: String)

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): AuthTokens

    /** Appel synchrone, utilisé par l'Authenticator. */
    @POST("auth/refresh")
    fun refresh(@Body body: RefreshRequest): Call<AuthTokens>

    @POST("auth/logout")
    suspend fun logout()
}
