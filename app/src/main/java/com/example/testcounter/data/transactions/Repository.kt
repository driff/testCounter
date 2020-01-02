package com.example.testcounter.data.transactions

import com.example.testcounter.data.models.Counter
import com.example.testcounter.data.network.NetworkService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class Repository @Inject constructor(val network: NetworkService) {

    private val TAG = this.javaClass.canonicalName

    fun fetchCounters() = network.fetchCounters().retry(2)

    fun addCounter(title: String) =
        network.postCounter(counter = Counter(title =  title))
            .retry(2)

    fun increaseCounter(counter: Counter) = network.increaseCounter(counter)
        .retry(2)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun decreaseCounter(counter: Counter) = network.decreaseCounter(counter)
        .retry(2)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun deleteCounter(counter: Counter) = network.deleteCounter(counter)
        .retry(2)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

}