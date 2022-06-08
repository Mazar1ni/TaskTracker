package com.github.mazar1ni.tasktracker.core.util

import io.gsonfire.GsonFireBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkManager {

    inline fun <reified T> create(url: String): T {

        val gson = GsonFireBuilder()
            .createGsonBuilder()
            .serializeNulls()
            .setLenient()
            .create()

        val okHttpClient = OkHttpClient.Builder()
            .apply {
                // TODO: accept logging only debug build
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(httpLoggingInterceptor)

                // TODO: move to constants
                readTimeout(30, TimeUnit.SECONDS)
                writeTimeout(30, TimeUnit.SECONDS)
                connectTimeout(30, TimeUnit.SECONDS)
            }
            .addInterceptor { chain ->
                // TODO: check network
                // TODO: add retry
                // TODO: add log
                // TODO: add compression
                chain.proceed(chain.request())
            }


        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient.build())
            .build()
            .create(T::class.java)
    }

}