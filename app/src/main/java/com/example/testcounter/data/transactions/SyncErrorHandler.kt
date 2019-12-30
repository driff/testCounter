package com.example.testcounter.data.transactions

import android.util.Log
import com.example.testcounter.data.models.Counter
import com.example.testcounter.data.models.UnsyncedChanges
import io.reactivex.Observable
import io.reactivex.Single
import io.realm.Realm
import java.lang.Exception
import javax.inject.Inject

class SyncErrorHandler @Inject constructor() {

    private val TAG = this.javaClass.canonicalName

    fun handleFailure(counter: Counter, type: SyncType){
        Log.d(TAG, "Handle Failure")
        //TODO: save ids to unsyncedCounters and return counter (might change it to unsyncedCounter)
        try {
            Realm.getDefaultInstance().executeTransactionAsync({
                it.copyToRealm(UnsyncedChanges(counter.localId, counter.serverId, type.type))
            }, { ->
                // TODO: Start worker
                Log.d(TAG, "success sync error")
            }, {
                Log.e(TAG, it.message?: "error en SyncErrorHandler")
            })
        } catch (e: Exception) {
            Log.e(TAG, e.message?: "error en SyncErrorHandler")

        }
    }

}

enum class SyncType(val type: Int) {
    CREATE(0),
    INCREASE(1),
    DECREASE(2),
    DELETE(3)
}

enum class SyncStatus(val status: Int) {
    UNSYNCED(0),
    SYNCED(1)
}