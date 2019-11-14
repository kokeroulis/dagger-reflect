package com.example;

import dagger.Binds;
import dagger.Component;
import dagger.Provides;

import javax.inject.Inject;

@Component(modules = MemberGenericInjectionParameter.ModuleThing.class)
public interface MemberGenericInjectionParameter {

    FooActivity fooActivity();

    abstract class BaseActivity<T, R> {


        @Inject
        Renderer<T, R> renderer;
    }

    class FooActivity extends BaseActivity<String, Integer> {

        public FooActivity() {
        }
    }


    interface Renderer<T, R> {}

    class ThingRenderer implements Renderer<String, Integer> {

        @Inject
        public ThingRenderer() {
        }
    }


    @dagger.Module
    abstract class ModuleThing {

        @Binds
        abstract Renderer<String, Integer> bindThingRenderer(ThingRenderer renderer);

        @Provides
        static FooActivity fooActivity() {
            return new FooActivity();
        }
    }
}
