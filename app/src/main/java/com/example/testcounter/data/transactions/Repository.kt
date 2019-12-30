package com.example.testcounter.data.transactions

import android.util.Log
import com.example.testcounter.data.models.Counter
import com.example.testcounter.data.models.UnsyncedChanges
import com.example.testcounter.data.network.NetworkService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import javax.inject.Inject

class Repository @Inject constructor(val network: NetworkService, val errorHandler: SyncErrorHandler) {

    private val TAG = this.javaClass.canonicalName
    private val disposable = CompositeDisposable()

    private val realm: Realm = Realm.getDefaultInstance()

    // TODO: Update counter with the serverId
    fun addCounter(id: String, title: String) {
        realm.executeTransactionAsync({
            val newCounter = it.createObject(Counter::class.java, id)
            newCounter.title = title
            newCounter.count = 0
            it.copyToRealmOrUpdate(newCounter)
        }, {  ->
            network.postCounter(counter = Counter(id, title))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({list: List<Counter>? ->
                    Log.d(TAG, "list: ${list.toString()}")
                }, {
                    Log.e(TAG, it.message?: "Error")
                    errorHandler.handleFailure(Counter(id, title), SyncType.CREATE)
                })
        })
    }

    // TODO: Create a table to store unsynced changes, when persist on server fails, save the update in this table
    // and call work manager to handle background sync, if no serverId is provided it means the counter has not been synced
    // so the app needs to create it first, since the server only handles increase by 1 and decrease by 1, each update needs to be stored locally
    // after all the data has been synced, update the counter that needs to be updated
    fun increaseCounter(counter: Counter) {
        realm.executeTransactionAsync ({
            Log.d("Repo", "repo transaction")
            val updateCounter = it.where(Counter::class.java).equalTo("localId", counter.localId).findFirst()
            updateCounter?.count = updateCounter?.count?.plus(1)
        }, {  ->
            network.increaseCounter(counter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({list: List<Counter>? ->
                    Log.d(TAG, "list: ${list.toString()}")
                }, {
                    Log.e(TAG, it.message?: "Error")
                    errorHandler.handleFailure(counter, SyncType.INCREASE)
                })
        })
    }

    fun decreaseCounter(counter: Counter) {
        realm.executeTransactionAsync ({
            val updateCounter = it.where(Counter::class.java).equalTo("localId", counter.localId).findFirst()
            updateCounter?.count = updateCounter?.count?.minus(1)
        }, {  ->
            network.decreaseCounter(counter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({list: List<Counter>? ->
                    Log.d(TAG, "list: ${list.toString()}")
                }, {
                    Log.e(TAG, it.message?: "Error")
                    errorHandler.handleFailure(counter, SyncType.DECREASE)
                })
        })
    }

    fun deleteCounter(counter: Counter) {
        realm.executeTransactionAsync ({
            it.where(Counter::class.java).equalTo("localId", counter.localId).findFirst()?.deleteFromRealm()
        }, { ->
            network.deleteCounter(counter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({list ->
                    Log.d(TAG, "list: ${list?.toString()}")
                }, {
                    Log.e(TAG, it.message?: "Error")
                    errorHandler.handleFailure(counter, SyncType.DELETE)
                })
        })
    }

    fun getAllCounters() = this.realm.where(Counter::class.java).findAll().asFlowable()

    fun getAllPendingSync() = this.realm.where(UnsyncedChanges::class.java).findAll().asFlowable()

    fun clear() {
        realm.close()
        disposable.clear()
    }

}