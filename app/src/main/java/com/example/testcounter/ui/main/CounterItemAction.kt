package com.example.testcounter.ui.main

import android.util.Log
import com.example.testcounter.data.models.Counter
import com.example.testcounter.di.PerActivity
import javax.inject.Inject

@PerActivity
class CounterItemAction @Inject constructor(private val viewModel: MainViewModel) : CounterAdapter.ItemActions {

    override fun increase(counter: Counter) {
        viewModel.updateCounter(counter)
    }

    override fun decrease(counter: Counter) {
        viewModel.updateCounter(counter, false)
    }

    override fun delete(counter: Counter) {
        viewModel.deleteCounter(counter)
    }
}