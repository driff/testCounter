package com.example.testcounter

import android.app.Application
import com.example.testcounter.di.components.AppComponent
import com.example.testcounter.di.components.DaggerAppComponent
import com.example.testcounter.di.modules.AppModule

class CounterApp: Application() {

    companion object {
        lateinit var app: CounterApp
            private set
    }

    lateinit var component: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        app = this
        this.component = createComponent()
    }

    protected open fun createComponent(): AppComponent {
        return DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

}