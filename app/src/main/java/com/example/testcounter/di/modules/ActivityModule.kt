package com.example.testcounter.di.modules

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import com.example.testcounter.di.PerActivity
import com.example.testcounter.ui.main.MainFragment
import com.example.testcounter.ui.main.MainViewModel
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activityContext: Context) {

    @Provides
    @PerActivity
    @JvmStatic
    fun provideActivityContext(): Context {
        return activityContext
    }

    @JvmStatic
    @PerActivity
    @Provides fun provideViewModel(fragment: MainFragment): MainViewModel {
        return ViewModelProviders.of(fragment).get(MainViewModel::class.java)
    }

}