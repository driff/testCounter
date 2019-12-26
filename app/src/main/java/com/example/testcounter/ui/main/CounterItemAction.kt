package com.example.testcounter.ui.main

import com.example.testcounter.data.models.Counter
import com.example.testcounter.di.PerActivity
import io.reactivex.subjects.AsyncSubject
import javax.inject.Inject

@PerActivity
class CounterItemAction @Inject constructor() : CounterAdapter.ItemActions {

    val onIncrease = AsyncSubject.create<Counter>()
    val onDecrease = AsyncSubject.create<Counter>()
    val onDelete = AsyncSubject.create<Counter>()

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