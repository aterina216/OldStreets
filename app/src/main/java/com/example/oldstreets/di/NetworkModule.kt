package com.example.oldstreets.di

import com.example.oldstreets.data.remote.api.CommonsApi
import com.example.oldstreets.data.remote.api.DataApi
import com.example.oldstreets.data.remote.api.PastVuApi
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory {
        return MoshiConverterFactory.create(moshi)
    }


    @Provides
    @Singleton
    fun provideDataApi(client: OkHttpClient, factory: MoshiConverterFactory): DataApi {
        return Retrofit.Builder()
            .baseUrl("https://suggestions.dadata.ru/")
            .client(client)
            .addConverterFactory(factory)
            .build()
            .create(DataApi::class.java)
    }


    @Provides
    @Singleton
    fun providePastVuApi(moshi: MoshiConverterFactory): PastVuApi {
        return Retrofit.Builder()
            .baseUrl("https://api.pastvu.com/")
            .addConverterFactory(moshi)
            .build()
            .create(PastVuApi::class.java)

    }

    @Provides
    @Singleton
    fun provideCommonsApi(okHttpClient: OkHttpClient): CommonsApi {
        return Retrofit.Builder()
            .baseUrl("https://commons.wikimedia.org/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(CommonsApi::class.java)
    }
}