package com.example.testcounter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.testcounter.data.models.Counter
import com.example.testcounter.di.components.ActivityComponent
import com.example.testcounter.ui.factory.ViewModelFactory
import com.example.testcounter.ui.main.CounterTotal
import com.example.testcounter.ui.main.MainFragment
import com.example.testcounter.ui.main.MainViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    lateinit var activityComponent: ActivityComponent
    @Inject lateinit var viewModelFactory: ViewModelFactory<MainViewModel>
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent = (application as CounterApp).appComponent.activityComponent().create()
        activityComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.getCountTotal().observe(this, updateCounterTotals)
        setTitleAndSubtitle(0, 0)
    }

    private val updateCounterTotals = Observer<CounterTotal> { (sumTotal: Int, list: List<Counter>) ->
        setTitleAndSubtitle(list.size, sumTotal)
    }

    private fun setTitleAndSubtitle(count: Int, sumTotal: Int) {
        supportActionBar?.title = String.format(baseContext.getString(R.string.app_title), count)
        supportActionBar?.subtitle = String.format(baseContext.getString(R.string.app_subtitle), sumTotal)
    }
}
