package com.example;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;
import dagger.multibindings.IntoMap;
import dagger.multibindings.Multibinds;
import dagger.multibindings.StringKey;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@ComponentInheritedMultibindingMap.ComponentScope
@Component(modules = ComponentInheritedMultibindingMap.Module1.class)
interface ComponentInheritedMultibindingMap {
    ChildComponent.Builder childComponent();

    Dep2 dep2();

    @Retention(RUNTIME) // Allows runtimes to have specialized behavior interoperating with Dagger.
    @Target({METHOD, TYPE})
    @Documented
    @interface ComponentScope {}

    @Module
    abstract class Module1 {

        @Provides
        @ComponentInheritedMultibindingMap.ComponentScope
        @IntoMap
        @StringKey("1")
        static String one() {
            return "one";
        }

        @Provides
        @IntoMap
        @ComponentInheritedMultibindingMap.ComponentScope
        @StringKey("2")
        static String two() {
            return "two";
        }

        @Provides
        static Dep2 dep2(Map<String, String> stringStringMap) {
            return new Dep2(stringStringMap);
        }

    }

    @Subcomponent(modules = ChildComponent.ChildModule.class)
    interface ChildComponent {
        SomeDep someDep();

        @Subcomponent.Builder
        interface Builder {

            ChildComponent build();
        }

        @Module
        abstract class ChildModule {

            @Multibinds
            abstract Map<String, String> someStuff();

            @Provides
            @IntoMap
            @StringKey("3")
            static String three() {
                return "three";
            }

            @Provides
            @IntoMap
            @StringKey("4")
            static String four() {
                return "four";
            }

            @Provides
            static SomeDep providesSomeDep(Map<String, String> stringStringMap) {
                return new SomeDep(stringStringMap);
            }
        }
    }


    class SomeDep {
        private final Map<String, String> stringStringMap;

        public SomeDep(Map<String, String> stringStringMap) {
            this.stringStringMap = stringStringMap;
        }


        public Map<String, String> lastTwo() {
            Map<String, String> map = new LinkedHashMap<>();
            for (Map.Entry<String, String> stringStringEntry : stringStringMap.entrySet()) {

                if (!stringStringEntry.getKey().equals("3") && !stringStringEntry.getKey().equals("4")) {
                    map.put(stringStringEntry.getKey(), stringStringEntry.getValue());
                }
            }

            return map;
        }
    }

    class Dep2 {

        private final Map<String, String> stringStringMap;

        public Dep2(Map<String, String> stringStringMap) {
            this.stringStringMap = stringStringMap;
        }

        public Map<String, String> getStringStringMap() {
            return stringStringMap;
        }
    }
}
