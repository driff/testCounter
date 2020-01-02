package com.example.testcounter.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
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
import com.example.testcounter.utils.DRAWABLE_RIGHT
import com.example.testcounter.utils.handleItemSearch
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.counter_fragment.*
import javax.inject.Inject

class MainFragment : Fragment() {

    val disposables = CompositeDisposable()

    @Inject
    lateinit var counterAdapter: CounterAdapter

    @Inject
    lateinit var itemActions: CounterItemAction

    @Inject lateinit var viewModelFactory: ViewModelFactory<MainViewModel>

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
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
            Log.d(TAG, "Viewmodel ref: $viewModel")
        }
        disposables.addAll( itemActions.onIncrease.subscribe {
            viewModel.updateCounter(it, true)
        }, itemActions.onDecrease.subscribe{
            viewModel.updateCounter(it, false)
        }, itemActions.onDelete.subscribe{
            viewModel.deleteCounter(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countersRecycler.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
            adapter = counterAdapter
        }
        edtNewCounterTitle.handleItemSearch(this::onItemSearch)
    }

    fun addCounter() {
        if(!edtNewCounterTitle.text.isNullOrEmpty() && edtNewCounterTitle.text?.length!! < 30) {
            this.viewModel.addNewCounter(edtNewCounterTitle.text.toString()).also {
                edtNewCounterTitle.text?.clear()
            }
        }
        // TODO: Add snackbar with message for else case
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.counterList.observe(this, countersObserver)
    }

    // Observers
    private val countersObserver = Observer<List<Counter>> {
        Log.d(TAG, "list changed!!")
        counterAdapter.setCounters(it)
    }

    private fun onItemSearch(event: MotionEvent, view: TextInputEditText): Boolean {
        if(event.rawX >= (view.right - view.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
            addCounter()
            return true
        }
        return false
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}
