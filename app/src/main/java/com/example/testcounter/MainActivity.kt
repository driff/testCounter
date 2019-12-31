package com.example.testcounter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        supportActionBar?.title = String.format(baseContext.getString(R.string.app_title), count)
        supportActionBar?.subtitle = String.format(baseContext.getString(R.string.app_subtitle), sumTotal)
    }

}
