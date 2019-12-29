package com.example.testcounter.ui.main

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testcounter.MainActivity
import com.example.testcounter.R
import com.example.testcounter.data.models.Counter
import com.example.testcounter.ui.factory.ViewModelFactory
import kotlinx.android.synthetic.main.counter_fragment.*
import kotlinx.android.synthetic.main.main_fragment.*
import javax.inject.Inject

class MainFragment : Fragment() {

    @Inject
    lateinit var counterAdapter: CounterAdapter

    @Inject
    lateinit var itemActions: CounterItemAction

    @Inject
    lateinit var viewModeFactory: ViewModelFactory<MainViewModel>

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
        (activity!! as MainActivity).activityComponent.inject(this).also {
            viewModel = ViewModelProviders.of(this, viewModeFactory).get(MainViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countersRecycler.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
            adapter = counterAdapter
        }
        fab.setOnClickListener(this::fabAction)
    }

    fun fabAction(view: View) {
        this.viewModel.addNewCounter(edtNewCounterTitle.text.toString()).also {
            edtNewCounterTitle.text?.clear()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
        viewModel.counterList.observe(this, countersObserver)
        viewModel.countTotals.observe(this, totalsObserver)
        Log.d(TAG, "Activity created...")
    }

    // Observers
    private val countersObserver = Observer<List<Counter>> {
        Log.d(TAG, "list changed!!")
        counterAdapter.setCounters(it)
    }

    private val totalsObserver = Observer<CounterTotals> {
        Log.d(TAG, "Total: ${it.sumTotal}")
    }

}
