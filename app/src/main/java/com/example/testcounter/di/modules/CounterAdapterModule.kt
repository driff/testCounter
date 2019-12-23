package com.example.testcounter.di.modules

import com.example.testcounter.ui.main.CounterAdapter
import dagger.Module
import dagger.Provides

@Module
object CounterAdapterModule {

    @JvmStatic
    @Provides
    fun provideCounterAdapter(actions: CounterAdapter.CounterItemActions): CounterAdapter {
        return CounterAdapter(listOf(), actions)
    }

}