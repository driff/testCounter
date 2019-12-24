package com.example.testcounter.di.modules

import androidx.lifecycle.ViewModelProviders
import com.example.testcounter.ui.main.MainFragment
import com.example.testcounter.ui.main.MainViewModel
import dagger.Module
import dagger.Provides

@Module
object FragmentModule {

    @JvmStatic
    @Provides fun providesFragment(): MainFragment {
        return MainFragment.newInstance()
    }

}