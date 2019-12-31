package com.example.testcounter.data.transactions

import com.example.testcounter.data.models.Counter
import com.example.testcounter.data.network.NetworkService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class Repository @Inject constructor(val network: NetworkService) {

    private val TAG = this.javaClass.canonicalName

    fun fetchCounters() = network.fetchCounters().retry(2)

    // TODO: Update counter with the serverId
    fun addCounter(title: String) =
        network.postCounter(counter = Counter(title =  title))
            .retry(2)

    // TODO: Create a table to store unsynced changes, when persist on server fails, save the update in this table
    // and call work manager to handle background sync, if no serverId is provided it means the counter has not been synced
    // so the app needs to create it first, since the server only handles increase by 1 and decrease by 1, each update needs to be stored locally
    // after all the data has been synced, update the counter that needs to be updated
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