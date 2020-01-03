package com.example.testcounter.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testcounter.R
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

    private val TAG = this.javaClass.canonicalName
    private val counterList: MutableLiveData<List<Counter>> = MutableLiveData()
    fun getCounters(): LiveData<List<Counter>> = counterList
    private val countTotals: MutableLiveData<CounterTotals> = MutableLiveData()
    fun getCountTotal(): LiveData<CounterTotals> = countTotals
    private val errors: MutableLiveData<Int> = MutableLiveData()
    fun getErrors(): LiveData<Int> = errors
    init {
        loadCounters()
    }

    private fun loadCounters() {
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
            this.errors.postValue(R.string.msg_no_connection_text)
        })

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}

data class CounterTotals(val count: Int = 0, val sumTotal: Int = 0, val counters: List<Counter>)