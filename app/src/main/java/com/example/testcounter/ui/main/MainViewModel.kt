package com.example.testcounter.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testcounter.data.models.Counter
import com.example.testcounter.data.transactions.Repository
import com.example.testcounter.di.PerActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@PerActivity
class MainViewModel @Inject constructor(private val repo: Repository) : ViewModel() {

    private val disposables = CompositeDisposable()

    val TAG = this.javaClass.canonicalName
    val counterList: MutableLiveData<List<Counter>> = MutableLiveData()
    val countTotals: MutableLiveData<CounterTotals> = MutableLiveData()

    init {
        loadCounters()
    }

    private fun loadCounters() {
        Log.d(TAG, "Viewmodel Init")
        disposables.add(countersObserverHandler(repo.fetchCounters().toObservable()))
    }

    /**
     * updates the counter value, if no 2nd parameter is passed, it will increase the value
     */
    fun updateCounter(counter: Counter, increase: Boolean = true) {
        val obs = if (increase) {
            repo.increaseCounter(counter)
        } else {
            repo.decreaseCounter(counter)
        }
        disposables.add(countersObserverHandler(obs.toObservable()))
    }

    fun deleteCounter(counter: Counter) {
        disposables.add(countersObserverHandler(repo.deleteCounter(counter).toObservable()))
    }

    fun addNewCounter(title: String) {
        if (!title.isEmpty()) {
            disposables.add( countersObserverHandler(repo.addCounter(title).toObservable()))
        }
    }

    private fun countersObserverHandler(obs: Observable<List<Counter>>) = obs.concatMap {list ->
        if(list.isEmpty()) {
            return@concatMap Observable.just(CounterTotals(0, 0, listOf()))
        }
        Observable.fromIterable(list).reduce(CounterTotals(list.size, 0, list),
            { t1, t2 ->
                return@reduce CounterTotals(list.size, t1.sumTotal + t2.count, list)
            }).toObservable()
    }.firstOrError()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe ({ t ->
            counterList.postValue(t.counters)
            countTotals.postValue(t)
        }, { err->
            Log.d(TAG, err.message?: "Viewmodel Error")
        })

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}

data class CounterTotals(val count: Int = 0, val sumTotal: Int = 0, val counters: List<Counter>)