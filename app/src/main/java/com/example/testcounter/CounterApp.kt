package com.example.testcounter

import androidx.multidex.MultiDexApplication
import com.example.testcounter.di.components.AppComponent
import com.example.testcounter.di.components.DaggerAppComponent
import io.realm.Realm

class CounterApp: MultiDexApplication() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}