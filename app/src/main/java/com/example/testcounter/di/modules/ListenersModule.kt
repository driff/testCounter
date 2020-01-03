package com.example.testcounter.di.modules

import com.example.testcounter.di.PerActivity
import com.example.testcounter.ui.main.CounterAdapter
import com.example.testcounter.ui.main.CounterListActions
import dagger.Binds
import dagger.Module

@Module
abstract class ListenersModule {

    @PerActivity
    @Binds
    abstract fun provideItemAction(listActions: CounterListActions): CounterAdapter.ItemActions


}