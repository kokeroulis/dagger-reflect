package com.example;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = ExampleActivity.ExampleActivityModule.class)
    abstract ExampleActivity activity();
}
