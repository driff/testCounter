package com.example.testcounter.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testcounter.R
import com.example.testcounter.data.models.Counter
import kotlinx.android.synthetic.main.counter_item.view.*

class CounterAdapter(private val counters: List<Counter>, val actionListener: CounterItemActions): RecyclerView.Adapter<CounterAdapter.CounterHolder>() {

    interface CounterItemActions {
        fun increase(counter: Counter)
        fun decrease(counter: Counter)
        fun delete(counter: Counter)
    }

    override fun getItemCount(): Int {
        return counters.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CounterHolder {
        return CounterHolder(LayoutInflater.from(parent.context).inflate(R.layout.counter_item, parent, false))
    }

    override fun onBindViewHolder(holder: CounterHolder, position: Int) {
        val counter = counters[position]

    }

    inner class CounterHolder(val view: View): RecyclerView.ViewHolder(view) {

        fun bindView(data: Counter) {
            // TODO: set data
            view.btnIncrease.setOnClickListener { actionListener.increase(data) }
        }

    }

}