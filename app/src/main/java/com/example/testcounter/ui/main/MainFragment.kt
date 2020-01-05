package com.example.testcounter.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.counter_fragment.*
import javax.inject.Inject

class MainFragment : Fragment() {

    private val disposables = CompositeDisposable()

    @Inject
    lateinit var counterAdapter: CounterAdapter

    @Inject
    lateinit var itemListActions: CounterListActions

    @Inject lateinit var viewModelFactory: ViewModelFactory<MainViewModel>

    lateinit var viewModel: MainViewModel

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.counter_fragment, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity!! as MainActivity).activityComponent.inject(this).also {
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        }
        loadObservers()
    }

    private fun loadObservers() {
        disposables.addAll( itemListActions.onIncrease.subscribe {
            showProgressBar(true)
            viewModel.updateCounter(it, true)
        }, itemListActions.onDecrease.subscribe{
            showProgressBar(true)
            viewModel.updateCounter(it, false)
        }, itemListActions.onDelete.subscribe{
            showDeleteDialog(it)
        }, itemListActions.onRefresh.subscribe{
            viewModel.refreshCounters()
            showProgressBar(true)
        })
        viewModel.getErrors().observe(this, errorObserver)
    }

    private fun showDeleteDialog(it: Counter) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.delete_counter_title)
            .setMessage(R.string.delete_counter_message)
            .setPositiveButton(R.string.positive_action) { dialog, _ ->
                showProgressBar(true)
                viewModel.deleteCounter(it)
                dialog.dismiss()
            }
            .setNegativeButton(R.string.negative_action) {dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showProgressBar(show: Boolean) {
        progressBar.visibility = if(show) View.VISIBLE else View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countersRecycler.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
            adapter = counterAdapter
            swipeRefreshContainer.setOnRefreshListener(itemListActions)
        }
        edtNewCounterTitle.handleItemSearch(this::onItemSearch)
    }

    private fun addCounter() {
        if(!edtNewCounterTitle.text.isNullOrEmpty() && edtNewCounterTitle.text?.length!! <= 30) {
            this.viewModel.addNewCounter(edtNewCounterTitle.text.toString()).also {
                edtNewCounterTitle.text?.clear()
                edtNewCounterTitle.clearFocus()
                showProgressBar(true)
            }
        } else {
            showSnackbarMessage(R.string.msg_no_input_text, Snackbar.LENGTH_LONG)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getCounters().observe(this, countersObserver)
    }

    // Observers
    private val countersObserver = Observer<List<Counter>> {
        counterAdapter.setCounters(it)
        swipeRefreshContainer.isRefreshing = false
        showProgressBar(false)
    }

    private val errorObserver = Observer<Int> {
        showSnackbarMessage(it)
        swipeRefreshContainer.isRefreshing = false
        showProgressBar(false)
    }

    private fun onItemSearch(event: MotionEvent, view: TextInputEditText): Boolean {
        if(event.rawX >= (view.right - view.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
            addCounter()
            return true
        }
        return false
    }

    private fun showSnackbarMessage(msg: Int, length: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(main, msg, length).show()
    }


    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }
}
