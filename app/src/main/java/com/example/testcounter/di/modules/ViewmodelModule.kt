package com.example.testcounter.di.modules

import androidx.lifecycle.ViewModelProviders
import com.example.testcounter.di.PerActivity
import com.example.testcounter.ui.main.MainFragment
import com.example.testcounter.ui.main.MainViewModel
import dagger.Module
import dagger.Provides

@Module
object ViewmodelModule {

    @Provides
    @PerActivity
    fun providesViewModel(fragment: MainFragment) = ViewModelProviders.of(fragment).get(
        MainViewModel::class.java)


}