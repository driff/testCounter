package com.example.testcounter.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testcounter.di.components.ActivityComponent
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import javax.inject.Singleton

@Module(subcomponents = [ActivityComponent::class])
object AppSubcomponents