package com.example;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.DispatchingAndroidInjector;

@Component(modules = {
        AndroidInjectionModule.class,
        ActivityBindingModule.class
})
interface AppComponent {

    void inject(ExampleApp app);

    DispatchingAndroidInjector<Object> androidInjector();
}
