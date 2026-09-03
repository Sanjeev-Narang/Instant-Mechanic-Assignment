package com.narang.instantmechanic.di

import com.narang.instantmechanic.data.MechanicApi
import com.narang.instantmechanic.data.AuthInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Minimal network graph.
 * Same OkHttp + Moshi + Retrofit stack as before, just provided by Hilt.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // TODO: point to your Firebase Cloud Function / Hosting rewrite
    // MUST end with "/"
    const val BASE_URL = "https://us-central1-instant-mechanic-demo.cloudfunctions.net/api/"

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor { msg -> Timber.tag("OkHttp").d(msg) }
            .apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor { null })
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi, client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    fun provideMechanicApi(retrofit: Retrofit): MechanicApi =
        retrofit.create(MechanicApi::class.java)
}
