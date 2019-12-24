package com.example.testcounter.di.components

import android.content.Context
import com.example.testcounter.data.models.network.NetworkService
import com.example.testcounter.di.modules.AppModule
import com.example.testcounter.di.modules.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
interface AppComponent {
    fun getAppContext(): Context
    fun getNetworkService(): NetworkService
}