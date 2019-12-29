package com.example.testcounter.di.modules

import com.example.testcounter.data.network.NetworkService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import javax.inject.Singleton

@Module
object NetworkModule {

    @Provides
    fun provideOkHttp(interceptor: HttpLoggingInterceptor): Call.Factory {
        return OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    fun provideInterceptor(): HttpLoggingInterceptor{
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS)
    }

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setDateFormat(DateFormat.MEDIUM, DateFormat.FULL)
            .create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, callFactory: Call.Factory): Retrofit {
        return Retrofit.Builder()
            .callFactory(callFactory)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("http://172.18.20.225:3000")
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): NetworkService = retrofit.create(NetworkService::class.java)

}