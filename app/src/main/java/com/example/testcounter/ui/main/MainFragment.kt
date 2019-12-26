package com.example.testcounter.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testcounter.MainActivity
import com.example.testcounter.R
import com.example.testcounter.data.models.Counter
import kotlinx.android.synthetic.main.counter_fragment.*
import kotlinx.android.synthetic.main.main_fragment.*
import javax.inject.Inject

class MainFragment : Fragment() {

    @Inject
    lateinit var counterAdapter: CounterAdapter

    @Inject
    lateinit var itemActions: CounterItemAction

    @Inject
    lateinit var viewModel: MainViewModel

    companion object {
        fun newInstance() = MainFragment()
        val TAG = this::class.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity!! as MainActivity).activityComponent.inject(this)
//        Log.d(TAG, "checking instance: adapter -> $counterAdapter | viewmodel: $viewModel")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countersRecycler.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = counterAdapter
        }
        fab.setOnClickListener(this::showCounterDialog)
    }

    fun showCounterDialog(view: View) {
        // TODO: Call dialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
        viewModel.counterList.observe(this, countersObserver)
    }

    // Observers
    val countersObserver = Observer<List<Counter>> {
        counterAdapter.setCounters(it)
    }

}
