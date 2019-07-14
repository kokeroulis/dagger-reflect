package com.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

 abstract class BaseActivity<P : Presenter, F: BaseActivity.Foo> : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentAndroidInjector: DispatchingAndroidInjector<Fragment>

     @Inject
     lateinit var presenterDelegate: PresenterDelegate<P>

     override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)
         val presenter = presenterDelegate.getPresenter()
    }

     override fun supportFragmentInjector(): AndroidInjector<Fragment> {
         return fragmentAndroidInjector
     }

     interface Foo
}