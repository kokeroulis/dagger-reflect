package com.example;

import com.example.ExampleService.ExampleServiceModule;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.DispatchingAndroidInjector;

@Component(modules = {
    ActivityBindingModule.class,
    ExampleServiceModule.class,
    AndroidInjectionModule.class,
    StringModule.class
})
interface AppComponent {
  DispatchingAndroidInjector<Object> androidInjector();
}
