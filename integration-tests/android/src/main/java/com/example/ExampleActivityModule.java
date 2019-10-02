package com.example;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface ExampleActivityModule {

    @ContributesAndroidInjector
    ExampleFragment fragment();
}
