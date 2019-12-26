package com.example.testcounter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testcounter.di.components.ActivityComponent
import com.example.testcounter.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    lateinit var activityComponent: ActivityComponent

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
    }

}
