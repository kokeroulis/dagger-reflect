package com.example;

import android.app.ActionBar;
import android.app.Activity;
import android.util.Log;

import javax.inject.Inject;

public class ExampleClass {

    @Inject
    ExampleClass() {}

    @Inject
    Activity activity;

    public void bar() {
        @SuppressWarnings("UnusedVariable")
        ActionBar actionBar = activity.getActionBar();
        Log.e("testaaaa", activity.toString());

        @SuppressWarnings("UnusedVariable")
        String foo = "asdasdasd";
    }
}