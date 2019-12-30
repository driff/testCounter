package com.example.testcounter.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testcounter.data.models.Counter
import com.example.testcounter.data.models.UnsyncedChanges
import com.example.testcounter.data.transactions.Repository
import com.example.testcounter.di.PerActivity
import com.example.testcounter.utils.unmanagedCopy
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import javax.inject.Inject

@PerActivity
class MainViewModel @Inject constructor(private val repo: Repository) : ViewModel() {

    private val disposables = CompositeDisposable()

    val TAG = this.javaClass.canonicalName
    val counterList: MutableLiveData<List<Counter>> = MutableLiveData()
    val countTotals: MutableLiveData<CounterTotals> = MutableLiveData()
    val unsynced: MutableLiveData<List<UnsyncedChanges>> = MutableLiveData()

    init {
        loadCounters()
    }

    private fun loadCounters() {
        val counters = repo.getAllCounters().map { it.toList() }.onErrorReturnItem(listOf())
        disposables.add(counters.subscribe {
            counterList.postValue(it)
        })
        disposables.add(counters.map {
            if(it.isEmpty()) {
                return@map CounterTotals(0, 0)
            } else {
                return@map CounterTotals(it.size, it.sumBy { current -> current.count ?: 0 })
            }
        }.subscribe { countTotals.postValue(it) })
        disposables.add(repo.getAllPendingSync().map { it.toList() }
            .subscribe{
                unsynced.postValue(it)
            })
    }

    /**
     * updates the counter value, if no 2nd parameter is passed, it will increase the value
     */
    fun updateCounter(counter: Counter, increase: Boolean = true) {
        if (increase) {
            repo.increaseCounter(counter.unmanagedCopy())
        } else {
            repo.decreaseCounter(counter.unmanagedCopy())
        }
    }

    fun deleteCounter(counter: Counter) {
        repo.deleteCounter(counter.unmanagedCopy())
    }

    fun addNewCounter(title: String) {
        if (!title.isEmpty()) {
            repo.addCounter(UUID.randomUUID().toString(), title)
        }
    }

    override fun onCleared() {
        repo.clear()
        disposables.clear()
        super.onCleared()
    }
}

data class CounterTotals(val count: Int = 0, val sumTotal: Int = 0)