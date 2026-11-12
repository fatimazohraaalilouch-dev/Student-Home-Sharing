package ma.shs.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ma.shs.app.BuildConfig
import ma.shs.app.data.AuthApi
import ma.shs.app.data.BearerInterceptor
import ma.shs.app.data.TokenAuthenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /** Client sans authenticator, pour l'appel de refresh (évite la boucle). */
    @Provides @Singleton @Named("plain")
    fun plainClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides @Singleton @Named("plain")
    fun plainRetrofit(@Named("plain") client: OkHttpClient): Retrofit =
        Retrofit.Builder().baseUrl(BuildConfig.API_BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()

    @Provides @Singleton
    fun authApi(@Named("plain") retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides @Singleton
    fun client(bearer: BearerInterceptor, authenticator: TokenAuthenticator): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(bearer)
            .authenticator(authenticator)
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            .build()

    @Provides @Singleton
    fun retrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder().baseUrl(BuildConfig.API_BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
}
