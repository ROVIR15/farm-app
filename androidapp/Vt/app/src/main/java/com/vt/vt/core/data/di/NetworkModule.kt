package com.vt.vt.core.data.di

import com.google.gson.GsonBuilder
import com.vt.vt.core.data.source.remote.ApiService
import com.vt.vt.utils.loggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    fun proviedOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            /*.addInterceptor { chain ->
                val original = chain.request();
                val authorized = original.newBuilder()
                    .addHeader("Cookie", "cookie-name=cookie-value")
                    .build();
                chain.proceed(authorized);
            }*/
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideApiService(client: OkHttpClient): ApiService {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://ekoarianto.tech/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}