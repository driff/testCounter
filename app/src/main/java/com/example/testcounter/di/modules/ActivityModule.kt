package com.example.testcounter.di.modules

import android.content.Context
import com.example.testcounter.MainActivity
import com.example.testcounter.di.PerActivity
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activityContext: Context) {

    @Provides
    @PerActivity
    fun provideActivityContext(): Context {
        return activityContext
    }

}