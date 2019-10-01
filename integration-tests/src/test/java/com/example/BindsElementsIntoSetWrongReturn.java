package com.example;

import dagger.Binds;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;

@Component(modules = BindsElementsIntoSetWrongReturn.Module1.class)
interface BindsElementsIntoSetWrongReturn {
  Set<String> strings();

  @Module
  abstract class Module1 {
    @Provides
    static Deque<String> strings() {
      ArrayDeque<String> strings = new ArrayDeque<>();
      strings.add("foo");
      return strings;
    }

    @Binds
    @ElementsIntoSet
    abstract String setStrings(Deque<String> foo);
  }
}
