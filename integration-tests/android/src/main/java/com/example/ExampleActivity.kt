package com.example

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup.LayoutParams
import android.widget.TextView

import javax.inject.Inject

import android.util.TypedValue.COMPLEX_UNIT_DIP
import android.view.Gravity.CENTER
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import javax.inject.Provider

class ExampleActivity : BaseActivity<ExamplePresenter, ExampleActivity.FooReal>() {
      @Inject lateinit var string: String

    @Inject lateinit var providerString: Provider<String>

    @Inject
    lateinit var exampleClass: ExampleClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val textView = TextView(this)
        textView.layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        textView.gravity = CENTER
        textView.setTextSize(COMPLEX_UNIT_DIP, 40f)
        textView.text = providerString.get();
        setContentView(textView)

        Log.e("testaaaa", toString())
        exampleClass!!.bar()
        presenterDelegate.getPresenter().foo()
        // startService(new Intent(this, ExampleService.class));
        addExampleFragment()
    }

    private fun addExampleFragment() {
        supportFragmentManager.beginTransaction().add(ExampleFragment(), ExampleActivity::class.java.simpleName).commit()
    }

    class FooReal : BaseActivity.Foo
}
