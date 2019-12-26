package com.example.testcounter.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testcounter.data.models.Counter
import com.example.testcounter.di.PerActivity
import javax.inject.Inject

@PerActivity
class MainViewModel @Inject constructor(): ViewModel() {

    val TAG = this.javaClass.canonicalName
    val counterList: MutableLiveData<List<Counter>> = MutableLiveData()
    val countTotals: MutableLiveData<CounterTotals> = MutableLiveData(CounterTotals())

    init {
        Log.d(TAG, "Init")
        loadCounters()
    }

    // TODO: calculate totals when the list updates

    private fun loadCounters() {
        Log.d(TAG, "Load Counters")
        counterList.postValue(listOf(
            Counter(1, "title 1", 0),
            Counter(2, "title 2", 0),
            Counter(3, "title 3", 0),
            Counter(4, "title 4", 0)
        ))
    }

    /**
     * updates the counter value, if no 2nd parameter is passed, it will increase the value
     */
    fun updateCounter(counter: Counter, increase: Boolean = true) {
        val refCopy = this.counterList.value
        if (!refCopy.isNullOrEmpty()) {
            when(increase) {
                true -> refCopy.find{ it.id == counter.id }?.count = counter.count?.plus(1)?: 1
                false -> refCopy.find{ it.id == counter.id }?.count = counter.count?.minus(1)?: 0
            }
            counterList.postValue(refCopy)
        }
    }

}

data class CounterTotals (val count: Int = 0, val sumTotal: Int = 0)