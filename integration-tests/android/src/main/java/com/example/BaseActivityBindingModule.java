package com.example;

import android.app.Activity;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class BaseActivityBindingModule<T extends Activity> {

    @Binds
    public abstract Activity bindActivity(T activity);
}
