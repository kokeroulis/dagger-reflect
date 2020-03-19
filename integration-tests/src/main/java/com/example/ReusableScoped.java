package com.example;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import dagger.Subcomponent;

@Component(modules = ReusableScoped.Module1.class)
interface ReusableScoped {
  Object object();

  Child child();

  I iInterface();

  @Subcomponent
  interface Child {
    Object object();

    Runnable runnable();
  }

  @Module
  abstract class Module1 {
    @Provides
    @Reusable
    static Object value() {
      return new Object();
    }

    @Provides
    @Reusable
    static Runnable runnable() {
      return new Runnable() {
        @Override
        public void run() {}
      };
    }

    @Provides
    @Reusable
    static I providesI() {
      return new Impl();
    }
  }

  interface I {}

  class Impl implements I {}

}