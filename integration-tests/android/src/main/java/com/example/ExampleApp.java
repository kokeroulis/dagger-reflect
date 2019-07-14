package com.example;

import android.app.Activity;
import android.app.Application;
import androidx.fragment.app.Fragment;
import dagger.Dagger;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;

import javax.inject.Inject;

public final class ExampleApp extends Application implements HasActivityInjector, HasSupportFragmentInjector {
  private AppComponent component;

  @Inject
  DispatchingAndroidInjector<Activity> dispatchingActivityInjector;
  @Inject
  DispatchingAndroidInjector<Fragment> dispatchingFragmentyInjector;

  @Override public void onCreate() {
    super.onCreate();

    component = Dagger.create(AppComponent.class);
    component.inject(this);
    @SuppressWarnings("UnusedVariable")
    String foo = "asddasdsa";
  }

  @Override
  public AndroidInjector<Activity> activityInjector() {
    return dispatchingActivityInjector;//component.androidInjector();
  }

  @Override
  public AndroidInjector<Fragment> supportFragmentInjector() {
    return dispatchingFragmentyInjector;
  }
}
