package com.example.testcounter

import androidx.multidex.MultiDexApplication
import com.example.testcounter.di.components.AppComponent
import com.example.testcounter.di.components.DaggerAppComponent

class CounterApp: MultiDexApplication() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

}