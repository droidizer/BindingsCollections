package com.blackbelt.bindings.utils

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.os.Handler
import android.os.Looper
import android.support.annotation.MainThread
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val mHasPendingChanges = AtomicBoolean()

    private val mHandler = Handler()

    override fun observe(owner: LifecycleOwner, observer: Observer<T>) {

        if (Looper.getMainLooper() != Looper.myLooper()) {
            mHandler.post({
                super.observe(owner, Observer<T> { t ->
                    if (mHasPendingChanges.compareAndSet(true, false)) {
                        observer.onChanged(t)
                    }
                })
            })
        } else {
            super.observe(owner, Observer<T> { t ->
                if (mHasPendingChanges.compareAndSet(true, false)) {
                    observer.onChanged(t)
                }
            })
        }
    }

    override fun setValue(value: T?) {
        mHasPendingChanges.set(true)
        super.setValue(value)
    }

    @MainThread
    fun call() {
        value = null
    }
}