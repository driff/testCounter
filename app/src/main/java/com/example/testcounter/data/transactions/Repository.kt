package com.example.testcounter.data.transactions

import android.util.Log
import com.example.testcounter.data.models.Counter
import com.example.testcounter.data.network.NetworkService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import javax.inject.Inject

class Repository @Inject constructor(val network: NetworkService) {

    val TAG = this.javaClass.canonicalName

    private val realm: Realm = Realm.getDefaultInstance()

    fun addCounter(id: Long, title: String) {
        realm.executeTransactionAsync({
            val newCounter = it.createObject(Counter::class.java, id)
            newCounter.title = title
            newCounter.count = 0
            it.copyToRealmOrUpdate(newCounter)
        }, {  ->
            network.postCounter(counter = Counter(id, title))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        })
    }

    fun updateCounter(id: Long?, value: Int) {
        realm.executeTransactionAsync {
                Log.d("Repo", "repo transaction")
                it.where(Counter::class.java).equalTo("localId", id).findFirst()?.count = value
        }
    }

    fun deleteCounter(id: Long) {
        realm.executeTransactionAsync {
            it.where(Counter::class.java).equalTo("localId", id).findFirst()?.deleteFromRealm()
        }
    }

    fun getAllCounters() = this.realm.where(Counter::class.java).findAll().asFlowable()

    fun clear() {
        realm.close()
    }

}