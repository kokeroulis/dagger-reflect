package com.example

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ExampleActivityModule : BaseActivityBindingModule<ExampleActivity>() {

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeExampleFragmentInjector(): ExampleFragment

    //@Binds
    //internal abstract fun bindsPresenter(example: ExamplePresenter): Presenter
}

