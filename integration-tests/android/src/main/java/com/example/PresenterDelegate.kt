package com.example

import javax.inject.Inject
import javax.inject.Provider


class PresenterDelegate<P : Presenter> @Inject constructor(private val provider: Provider<P>) {

    fun getPresenter(): P {
        return provider.get()
    }
}