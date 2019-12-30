package com.example.testcounter.data.transactions

import android.util.Log
import com.example.testcounter.data.models.Counter
import com.example.testcounter.data.network.NetworkService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import javax.inject.Inject

class Repository @Inject constructor(val network: NetworkService) {

    val TAG = this.javaClass.canonicalName

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
                })
        })
    }

    fun decreaseCounter(localId: String, serverId: String? = null) {
        realm.executeTransactionAsync ({
            Log.d("Repo", "repo transaction")
            val updateCounter = it.where(Counter::class.java).equalTo("localId", localId).findFirst()
            updateCounter?.count = updateCounter?.count?.minus(1)
        }, {  ->
            network.decreaseCounter(Counter(localId, serverId = serverId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({list: List<Counter>? ->
                    Log.d(TAG, "list: ${list.toString()}")
                }, {
                    Log.e(TAG, it.message?: "Error")
                })
        })
    }

    fun deleteCounter(id: String) {
        realm.executeTransactionAsync {
            it.where(Counter::class.java).equalTo("localId", id).findFirst()?.deleteFromRealm()
        }
    }

    fun getAllCounters() = this.realm.where(Counter::class.java).findAll().asFlowable()

    fun clear() {
        realm.close()
    }

    // TODO: Create a table to store unsynced changes, when persist on server fails, save the update in this table
    // and call work manager to handle background sync, if no serverId is provided it means the counter has not been synced
    // so the app needs to create it first, since the server only handles increase by 1 and decrease by 1, each update needs to be stored locally
    // after all the data has been synced, update the counter that needs to be updated
    // this method needs a fix, maybe get counter localId and serverId since counter cant be accessed in a different thread
    fun increaseCounter(localId: String, serverId: String? = null) {
        Log.d(TAG, "localId: $localId serverId: $serverId")
        realm.executeTransactionAsync ({
            Log.d("Repo", "repo transaction")
            val updateCounter = it.where(Counter::class.java).equalTo("localId", localId).findFirst()
            updateCounter?.count = updateCounter?.count?.plus(1)
        }, {  ->
            network.increaseCounter(Counter(localId, serverId = serverId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({list: List<Counter>? ->
                    Log.d(TAG, "list: ${list.toString()}")
                }, {
                    Log.e(TAG, it.message?: "Error")
                })
        })
    }

}