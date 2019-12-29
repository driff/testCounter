package com.example.testcounter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.example.testcounter.di.components.ActivityComponent
import com.example.testcounter.ui.main.CounterTotals
import com.example.testcounter.ui.main.MainFragment
import com.example.testcounter.ui.main.MainViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private val TAG = this.javaClass.canonicalName
    lateinit var activityComponent: ActivityComponent
    @Inject lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent = (application as CounterApp).appComponent.activityComponent().create()
        activityComponent .inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }

        viewModel.countTotals.observe(this, updateCounterTotals)

    }

    private val updateCounterTotals = Observer<CounterTotals> {(count: Int, sumTotal: Int) ->
        Log.d(TAG, "UpdateCounter -> ${supportActionBar?.isShowing}")
        supportActionBar?.title = "You have $count Counters"
        supportActionBar?.subtitle = "With a total of $sumTotal counts"
    }

}
