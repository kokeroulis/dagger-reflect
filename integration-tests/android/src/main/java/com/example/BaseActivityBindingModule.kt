package com.example

import android.app.Activity
import dagger.Binds
import dagger.Module

@Module
abstract class BaseActivityBindingModule<T : Activity> {

    @Binds
    @ActivityScope
    abstract fun bindActivity(activity: T): Activity
}
