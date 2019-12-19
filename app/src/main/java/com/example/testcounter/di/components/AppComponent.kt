package com.example.testcounter.di.components

import android.content.Context
import com.example.testcounter.di.modules.AppModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    fun getAppContext(): Context
}