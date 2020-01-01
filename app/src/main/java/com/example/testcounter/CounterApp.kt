package com.example.testcounter

import android.app.Application
import com.example.testcounter.di.components.AppComponent
import com.example.testcounter.di.components.DaggerAppComponent

class CounterApp: Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}