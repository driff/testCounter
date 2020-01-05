package com.example.testcounter.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testcounter.R
import com.example.testcounter.data.models.Counter
import com.example.testcounter.di.PerActivity
import kotlinx.android.synthetic.main.counter_item.view.*
import java.util.function.BiConsumer
import javax.inject.Inject

@PerActivity
class CounterAdapter @Inject constructor(val actionListener: ItemActions): RecyclerView.Adapter<CounterAdapter.CounterHolder>() {

    private var counters = mutableListOf<Counter>()

    interface ItemActions {
        fun increase(counter: Counter)
        fun decrease(counter: Counter)
        fun delete(counter: Counter)
    }

    fun setCounters(counters: List<Counter>) {
        this.counters = counters.toMutableList()
        notifyDataSetChanged()
    }

    fun removeCounter(counter: Counter) {
        val index = counters.indexOf(counter)
        counters.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun getItemCount(): Int {
        return counters.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CounterHolder {
        return CounterHolder(LayoutInflater.from(parent.context).inflate(R.layout.counter_item, parent, false))
    }

    override fun onBindViewHolder(holder: CounterHolder, position: Int) {
        val counter = counters[position]
        holder.bindView(counter)

    }

    inner class CounterHolder(private val view: View): RecyclerView.ViewHolder(view) {

        fun bindView(counter: Counter) {
            view.txvCount.text = (counter.count).toString()
            view.txvCounterTitle.text = counter.title
            view.btnIncrease.setOnClickListener { actionListener.increase(counter) }
            view.btnDecrease.setOnClickListener { actionListener.decrease(counter) }
            view.btnDeleteCounter.setOnClickListener { actionListener.delete(counter) }
        }
    }

}