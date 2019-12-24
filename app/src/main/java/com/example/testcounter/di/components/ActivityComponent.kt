package com.example.testcounter.di.components

import android.content.Context
import com.example.testcounter.MainActivity
import com.example.testcounter.di.PerActivity
import com.example.testcounter.di.modules.ActivityModule
import com.example.testcounter.di.modules.FragmentModule
import com.example.testcounter.di.modules.ViewmodelModule
import com.example.testcounter.ui.main.MainFragment
import dagger.Component

@PerActivity
@Component(modules = [ActivityModule::class, FragmentModule::class, ViewmodelModule::class], dependencies = [AppComponent::class])
interface ActivityComponent {
    fun inject(mainActivity: MainActivity)

    fun getFragment(): MainFragment

}