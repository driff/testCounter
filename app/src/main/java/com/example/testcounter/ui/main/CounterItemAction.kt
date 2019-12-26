package com.example.testcounter.ui.main

import android.util.Log
import com.example.testcounter.data.models.Counter
import com.example.testcounter.di.PerActivity
import javax.inject.Inject

@PerActivity
class CounterItemAction @Inject constructor(val viewModel: MainViewModel) : CounterAdapter.ItemActions {

    val TAG = this.javaClass.canonicalName

    override fun increase(counter: Counter) {
        Log.d(TAG, "increase: $counter")
        viewModel.updateCounter(counter)
    }

    override fun decrease(counter: Counter) {
        Log.d(TAG, "decrease: $counter")
        viewModel.updateCounter(counter, false)
    }

    override fun delete(counter: Counter) {
        Log.d(TAG, "delete: $counter")
    }
}