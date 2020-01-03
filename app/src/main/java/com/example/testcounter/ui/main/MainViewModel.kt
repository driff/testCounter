package com.example.testcounter.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testcounter.R
import com.example.testcounter.data.models.Counter
import com.example.testcounter.data.transactions.Repository
import com.example.testcounter.di.PerActivity
import com.example.testcounter.utils.sum
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerActivity
class MainViewModel @Inject constructor(private val repo: Repository) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val TAG = this.javaClass.canonicalName
    private val undoActions = mutableMapOf<String, Disposable>()
    private val counterList: MutableLiveData<List<Counter>> = MutableLiveData()
    fun getCounters(): LiveData<List<Counter>> = counterList
    private val countTotal: MutableLiveData<CounterTotal> = MutableLiveData()
    fun getCountTotal(): LiveData<CounterTotal> = countTotal
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
        val disposable = countersObserverHandler(
            Observable.just(counter)
                .delay(3L, TimeUnit.SECONDS)
                .concatMap { repo.deleteCounter(counter).toObservable() }
        )
        undoActions[counter.id] = disposable
        disposables.add(disposable)
    }

    fun addNewCounter(title: String) {
        if (!title.isEmpty()) {
            disposables.add( countersObserverHandler(repo.addCounter(title).toObservable()))
        }
    }

    fun undoDelete(counter: Counter) {
        disposables.remove(this.undoActions[counter.id]!!)
    }

    private fun countersObserverHandler(obs: Observable<List<Counter>>) = obs.concatMap {list ->
        if(list.isEmpty()) {
            return@concatMap Observable.just(CounterTotal(0, listOf()))
        }
        Observable.fromIterable(list).reduce(CounterTotal(0, list),
            { t1, t2 ->
                return@reduce CounterTotal(sum(t1.sumTotal , t2.count), list)
            }).toObservable()
    }.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe ({ t ->
            counterList.postValue(t.counters)
            countTotal.postValue(t)
        }, { err->
            Log.e(TAG, err.message?: "Viewmodel Error")
            this.errors.postValue(R.string.msg_no_connection_text)
        })

    override fun onCleared() {
        disposables.clear()
        undoActions.clear()
        super.onCleared()
    }
}

data class CounterTotal(val sumTotal: Int = 0, val counters: List<Counter>)