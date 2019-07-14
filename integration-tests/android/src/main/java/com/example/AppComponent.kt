package com.example

import dagger.Component
import dagger.android.AndroidInjectionModule

import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    ActivityBindingModule::class,
    StringModule::class]
)
internal interface AppComponent {
  //  fun androidInjector(): DispatchingAndroidInjector<Activity>

    fun inject(exampleApp: ExampleApp)
}
