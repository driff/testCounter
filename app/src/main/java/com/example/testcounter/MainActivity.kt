package com.example.testcounter

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testcounter.di.components.ActivityComponent
import com.example.testcounter.di.components.DaggerActivityComponent
import com.example.testcounter.di.modules.ActivityModule
import com.example.testcounter.ui.main.MainFragment
import dagger.Lazy
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    companion object {
        operator fun get(context: Context) = context.getSystemService("MainActivity") as MainActivity
    }

    lateinit var component: ActivityComponent
        private set

    @Inject
    lateinit var fragment: Lazy<MainFragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        this.component = DaggerActivityComponent.builder()
            .appComponent(CounterApp.app.component)
            .activityModule(ActivityModule(this))
            .build()
        this.component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment.get())
                .commitNow()
        }
    }

}
