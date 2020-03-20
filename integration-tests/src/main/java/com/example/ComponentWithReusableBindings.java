package com.example;

import dagger.*;

import javax.inject.Provider;
import javax.inject.Qualifier;


@Component(modules = ComponentWithReusableBindings.ReusableBindingsModule.class)
interface ComponentWithReusableBindings {

    @Qualifier
    @interface InParent {}

    @InParent
    Object reusableInParent();

    ChildOne childOne();

    int primitive();

    boolean unboxedPrimitive();

    Provider<Boolean> booleanProvider();

    @Subcomponent
    interface ChildOne {
        @InParent
        Object reusableInParent();
    }

    @Module
    class ReusableBindingsModule {
        @Provides
         @Reusable
        @InParent
        static Object inParent() {
            return new Object();
        }

        @Provides
        @Reusable
        static int primitive() {
            return 0;
        }

        @Provides
        @Reusable
        static Boolean boxedPrimitive() {
            return false;
        }
    }
}