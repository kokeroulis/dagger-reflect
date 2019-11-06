package com.example;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.Multibinds;
import dagger.multibindings.StringKey;

import javax.inject.Inject;
import java.util.Map;

@Component(modules = { MapMultiBinds.Module.class, MapMultiBinds.MultiBindsModule.class})
interface MapMultiBinds {
    Thing thing();

    @dagger.Module
    abstract class MultiBindsModule {
        @Multibinds
        abstract Map<String, Integer> getEmptyStringAndInteger();
    }

    @dagger.Module
    abstract class Module {

        @Provides
        @IntoMap
        @StringKey("some key")
        static Integer providesFoo() {
            return 1;
        }

        @Provides
        @IntoMap
        @StringKey("another key")
        static Integer providesFokko() {
            return 2;
        }
    }

    class Thing {

        private final Map<String, Integer> stringIntegerMap;

        @Inject
        public Thing(Map<String, Integer> stringIntegerMap) {
            this.stringIntegerMap = stringIntegerMap;
        }

        public Map<String, Integer> getStringIntegerMap() {
            return stringIntegerMap;
        }
    }
}
