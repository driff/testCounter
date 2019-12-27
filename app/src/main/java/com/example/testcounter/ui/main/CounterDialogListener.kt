package com.example.testcounter.ui.main

import com.example.testcounter.di.PerActivity
import javax.inject.Inject

@PerActivity
class CounterDialogListener @Inject constructor(val viewModel: MainViewModel): IDialogListener {



    override fun onDialogPositiveClick(title: String) {
        viewModel.createNewCounter(title)
    }

}