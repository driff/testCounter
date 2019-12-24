package com.example.testcounter.ui.main

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testcounter.CounterApp
import com.example.testcounter.MainActivity
import com.example.testcounter.R
import com.example.testcounter.di.components.DaggerActivityComponent
import javax.inject.Inject

class MainFragment : Fragment() {

    @Inject
    lateinit var counterAdapter: CounterAdapter

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
        MainActivity.get(context).component.inject(context as MainActivity)
        super.onAttach(context)
        Log.d(TAG, "checking instance: adapter -> $counterAdapter | viewmodel: $viewModel")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}
