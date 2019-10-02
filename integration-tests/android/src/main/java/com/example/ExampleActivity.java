package com.example;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

import javax.inject.Inject;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.view.Gravity.CENTER;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public final class ExampleActivity extends Activity implements HasAndroidInjector {
    @Inject
    DispatchingAndroidInjector<Object> androidInjector;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        TextView textView = new TextView(this);
        textView.setLayoutParams(new LayoutParams(MATCH_PARENT, MATCH_PARENT));
        textView.setGravity(CENTER);
        textView.setTextSize(COMPLEX_UNIT_DIP, 40);
        textView.setText("hello");
        setContentView(textView);
        getFragmentManager().beginTransaction().add(new ExampleFragment(), "some tag").commit();
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return androidInjector;
    }
}
