package com.example.testcounter.di.components

import android.content.Context
import com.example.testcounter.di.modules.AppSubcomponents
import com.example.testcounter.di.modules.NetworkModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, AppSubcomponents::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun activityComponent(): ActivityComponent.Factory

}