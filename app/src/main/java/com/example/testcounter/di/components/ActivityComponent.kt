package com.example.testcounter.di.components

import com.example.testcounter.MainActivity
import com.example.testcounter.di.PerActivity
import com.example.testcounter.di.modules.CounterActionsModule
import com.example.testcounter.ui.main.MainFragment
import dagger.Subcomponent

@PerActivity
@Subcomponent(modules = [CounterActionsModule::class])
interface ActivityComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }

    fun inject(mainActivity: MainActivity)
    fun inject(fragment: MainFragment)
}