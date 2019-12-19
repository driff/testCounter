package com.example.testcounter

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class CounterApp: Application() {

//    lateinit var dagger: AppComponent

    override fun onCreate() {
        super.onCreate()
//        dagger = AppComponent.create()
    }

}