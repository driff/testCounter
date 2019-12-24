package com.example.testcounter

import android.app.Application
import com.example.testcounter.di.components.AppComponent
import com.example.testcounter.di.components.DaggerAppComponent

class CounterApp: Application() {

    lateinit var dagger: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        dagger = DaggerAppComponent.builder().build()
    }

}