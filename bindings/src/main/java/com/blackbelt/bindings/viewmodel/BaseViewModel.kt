package com.blackbelt.bindings.viewmodel

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry
import com.blackbelt.bindings.notifications.ClickItemWrapper
import com.blackbelt.bindings.notifications.MessageWrapper
import com.blackbelt.bindings.utils.SingleLiveEvent


open class BaseViewModel : ViewModel(), Observable, LifecycleObserver {

    @delegate:Transient
    private val mCallbacks: PropertyChangeRegistry by lazy { PropertyChangeRegistry() }

    protected val mMessageNotifier = SingleLiveEvent<MessageWrapper>()

    protected var mItemClickNotifier: SingleLiveEvent<ClickItemWrapper> = SingleLiveEvent()

    override fun removeOnPropertyChangedCallback(p0: Observable.OnPropertyChangedCallback?) {
        mCallbacks.remove(p0)
    }

    override fun addOnPropertyChangedCallback(p0: Observable.OnPropertyChangedCallback?) {
        mCallbacks.add(p0)
    }

    fun notifyChange() {
        mCallbacks.notifyCallbacks(this, 0, null)
    }

    fun notifyPropertyChanged(fieldId: Int) {
        mCallbacks.notifyCallbacks(this, fieldId, null)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy() {
    }

    open fun handlerError(throwable: Throwable) {
        throwable.printStackTrace()
    }

    open fun getMessageDelegate() = mMessageNotifier

    open fun getItemClickDelegate() = mItemClickNotifier
}