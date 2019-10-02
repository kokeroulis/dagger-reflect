package com.example;

import dagger.Binds;
import dagger.Component;
import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;

@Component(
        modules = {
            ModuleIncludesParent.ThingModule.class,
            ModuleIncludesParent.SomeClassWithDependencyModule.class
        }
)
public interface ModuleIncludesParent {

   // Thing thing();

    @Module
    interface ThingModule {
        @Provides
        static Thing providesThing() {
            return new Thing();
        }

        @Binds
        ThingInterface bindThingInterface(Thing thing);
    }

    class Thing implements ThingInterface {

        @Override
        public void doSomething() { }
    }

    interface ThingInterface {

        void doSomething();

    }

    @Module
    interface SomeClassWithDependencyModule {
        @Provides
        static SomeClassWithDependency providesSomeClassWithDependency(ThingInterface thingInterface) {
            return new SomeClassWithDependency(thingInterface);
        }
    }


    class SomeClassWithDependency {
        private final ThingInterface thingInterface;

        @Inject
        public SomeClassWithDependency(ThingInterface thingInterface) {
            this.thingInterface = thingInterface;
        }

        void foo() {
            thingInterface.doSomething();
        }
    }
}
