package com.example;

import android.app.Application;
import dagger.Dagger;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

import javax.inject.Inject;

public final class ExampleApp extends Application implements HasAndroidInjector {
    private AppComponent component;

    @Inject
    DispatchingAndroidInjector<Object> androidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        component = Dagger.create(AppComponent.class);
        component.inject(this);
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return androidInjector;
    }
}
