package com.example.photoweatherapp.di

import com.example.photoweatherapp.data.WeatherApis
import com.example.photoweatherapp.utils.BASE_URL
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModules = module {

    single {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.create()
    }

    single {
        val builder = OkHttpClient.Builder()
        builder.readTimeout(20, TimeUnit.SECONDS)
        builder.connectTimeout(20, TimeUnit.SECONDS)

        //Your headers
        builder.addInterceptor { chain ->
            var request = chain.request()
            val url = request.url.newBuilder().build()
            request = request.newBuilder().url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Api-Access", "application/mobile")
                .build()
            chain.proceed(request)
        }

        //if debug mood show retrofit logging
        if (BuildConfig.DEBUG) {
            val debugInterceptor = HttpLoggingInterceptor()
            debugInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(debugInterceptor)
        }

        builder.build()
    }

    single {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(get()))
            .baseUrl(BASE_URL)
            .client(get())
            .build()
    }

    single {
        get<Retrofit>().create(WeatherApis::class.java)
    }

}