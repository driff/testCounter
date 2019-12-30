package com.example.testcounter.di.modules

import com.example.testcounter.di.PerActivity
import com.example.testcounter.ui.main.CounterAdapter
import com.example.testcounter.ui.main.CounterItemAction
import com.example.testcounter.ui.main.IDialogListener
import dagger.Binds
import dagger.Module

@Module
abstract class ListenersModule {

    @PerActivity
    @Binds
    abstract fun provideItemAction(actions: CounterItemAction): CounterAdapter.ItemActions


}