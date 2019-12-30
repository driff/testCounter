package com.example.testcounter.data.transactions

import com.example.testcounter.data.models.Counter
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class SyncErrorHandler @Inject constructor() {

    fun handleDecreaseFailed(counter: Counter){
        //TODO: save ids to unsyncedCounters and return counter (might change it to unsyncedCounter)
    }

}