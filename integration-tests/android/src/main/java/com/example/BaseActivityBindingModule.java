package com.example;

import android.app.Activity;
import dagger.Binds;
import dagger.Module;

@Module
public interface BaseActivityBindingModule<T extends Activity> {

    @Binds
    Activity bindActivity(T activity);
}
