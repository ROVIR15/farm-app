package com.vt.vt.core.data.di

import android.util.Log
import com.google.gson.GsonBuilder
import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.core.data.source.remote.dummy.auth.SessionPreferencesDataStoreManager
import com.vt.vt.utils.loggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    fun provideCookies(): CookieJar {
        return object : CookieJar {
            private val cookieStore = HashMap<String, List<Cookie>>()

            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                val cookies: List<Cookie>? = cookieStore[url.host]
                return cookies ?: ArrayList<Cookie>()
            }

            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                if (cookies.isNotEmpty()) {
                    cookieStore[url.host] = cookies
                }
            }
        }
    }

    @Provides
    fun provideOkHttpClient(pref: SessionPreferencesDataStoreManager): OkHttpClient {
        val client: OkHttpClient =
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .cookieJar(provideCookies())
                .authenticator { _, response ->
                    val newRequest = response.request.newBuilder()
                    var token: String?
                    runBlocking {
                        try {
                            token = pref.getToken()
                            if (!token.isNullOrEmpty()) {
                                newRequest.addHeader("Authorization", "Bearer $token")
                            } else {
                                pref.removeLoginState()
                            }
                        } catch (e: Exception) {
                            Log.e("NetworkError", e.message.toString())
                        }
                        newRequest.build()
                    }
                }
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build()

        return client
    }

    @Provides
    fun provideApiService(client: OkHttpClient): ApiService {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl("https://ekoarianto.tech/")
            .addConverterFactory(GsonConverterFactory.create(gson)).client(client).build()
        return retrofit.create(ApiService::class.java)
    }
}