package com.example.testcounter.di.modules

import com.example.testcounter.ui.main.MainFragment
import dagger.Module
import dagger.Provides

@Module
object FragmentModule {

    @JvmStatic
    @Provides fun providesFragment(): MainFragment {
        return MainFragment.newInstance()
    }

}