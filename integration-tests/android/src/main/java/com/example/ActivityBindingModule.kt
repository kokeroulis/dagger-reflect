package com.example

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [ExampleActivityModule::class])
    internal abstract fun contributeWelcomeActivityInjector(): ExampleActivity
}
