package com.example.testcounter

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.testcounter.ui.main.MainFragment

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito.mock

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {


    private lateinit var scenario: FragmentScenario<MainFragment>
    private val parentActivity = mock<MainActivity>(MainActivity::class.java)

    @Before
    fun setUp() {
        // Prepare fake data for SearchFragment
        // ...

        // Setup the FragmentScenario and attach any required mock data to it
        scenario = launchFragmentInContainer<MainFragment>(Bundle.EMPTY, R.style.Theme_AppCompat_Light)

    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.testcounter", appContext.packageName)
    }


}
