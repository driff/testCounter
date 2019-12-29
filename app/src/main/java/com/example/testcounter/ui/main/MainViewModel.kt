package com.example.testcounter.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testcounter.data.models.Counter
import com.example.testcounter.data.transactions.Repository
import com.example.testcounter.di.PerActivity
import io.reactivex.Observable
import io.realm.Realm
import javax.inject.Inject

@PerActivity
class MainViewModel @Inject constructor(val repo: Repository) : ViewModel() {

    val TAG = this.javaClass.canonicalName
    lateinit var counterList: LiveData<List<Counter>>
    lateinit var countTotals: LiveData<CounterTotals>

    init {
        Log.d(TAG, "Init")
        loadCounters()
    }

    // TODO: calculate totals when the list updates

    private fun loadCounters() {
        // TODO: Figure out how to reuse this observable
        val fetchCounters = repo.getAllCounters().map { it.toList() }
        counterList = LiveDataReactiveStreams.fromPublisher(fetchCounters)
        countTotals = LiveDataReactiveStreams.fromPublisher(
            fetchCounters.map {
                CounterTotals(it.size, it.reduce { acc, counter ->
                    acc.count = acc.count?.plus(counter?.count ?: 0)
                    acc
                }.count ?: 0)
            })
    }

    /**
     * updates the counter value, if no 2nd parameter is passed, it will increase the value
     */
    fun updateCounter(counter: Counter, increase: Boolean = true) {
        if (increase) {
            repo.updateCounter(counter.localId, counter.count?.plus(1)!!)
        } else {
            repo.updateCounter(counter.localId, counter.count?.minus(1)!!)
        }
    }

    fun deleteCounter(counter: Counter) {
        repo.deleteCounter(counter.localId!!)
    }

    fun addNewCounter(title: String) {
        if (!title.isEmpty()) {
            repo.addCounter((Math.random() * Math.random() * Math.random() * 10000).toLong(), title)
        }
    }

    override fun onCleared() {
        repo.onClear()
        super.onCleared()
    }
}

data class CounterTotals(val count: Int = 0, val sumTotal: Int = 0)