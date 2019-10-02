package com.example;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface ActivityBindingModule {

    @ContributesAndroidInjector(modules = ExampleActivityModule.class)
    ExampleActivity activity();

}
