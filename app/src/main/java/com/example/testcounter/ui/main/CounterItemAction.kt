package com.example.testcounter.ui.main

import android.util.Log
import com.example.testcounter.data.models.Counter
import com.example.testcounter.di.PerActivity
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@PerActivity
class CounterItemAction @Inject constructor() : CounterAdapter.ItemActions {

    val onIncrease = PublishSubject.create<Counter>()
    val onDecrease = PublishSubject.create<Counter>()
    val onDelete =   PublishSubject.create<Counter>()

    override fun increase(counter: Counter) {
        onIncrease.onNext(counter)
    }

    override fun decrease(counter: Counter) {
        onDecrease.onNext(counter)
    }

    override fun delete(counter: Counter) {
        onDelete.onNext(counter)
    }
}