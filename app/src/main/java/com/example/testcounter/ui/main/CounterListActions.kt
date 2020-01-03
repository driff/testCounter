package com.example.testcounter.ui.main

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.testcounter.data.models.Counter
import com.example.testcounter.di.PerActivity
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@PerActivity
class CounterListActions @Inject constructor() : CounterAdapter.ItemActions, SwipeRefreshLayout.OnRefreshListener {

    val onIncrease = PublishSubject.create<Counter>()
    val onDecrease = PublishSubject.create<Counter>()
    val onDelete =   PublishSubject.create<Counter>()
    val onRefresh =   PublishSubject.create<Boolean>()

    override fun increase(counter: Counter) {
        onIncrease.onNext(counter)
    }

    override fun decrease(counter: Counter) {
        onDecrease.onNext(counter)
    }

    override fun delete(counter: Counter) {
        onDelete.onNext(counter)
    }

    override fun onRefresh() {
        this.onRefresh.onNext(true)
    }
}