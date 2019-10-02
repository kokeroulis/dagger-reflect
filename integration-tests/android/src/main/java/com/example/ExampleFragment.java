package com.example;

import android.app.Fragment;
import android.content.Context;
import dagger.android.AndroidInjection;

public class ExampleFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        AndroidInjection.inject(this);
        super.onAttach(context);
    }
}
