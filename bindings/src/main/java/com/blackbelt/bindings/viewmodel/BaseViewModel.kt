package com.blackbelt.bindings.viewmodel

import android.arch.lifecycle.ViewModel
import com.blackbelt.bindings.notifications.ClickItemWrapper
import com.blackbelt.bindings.notifications.MessageWrapper
import com.blackbelt.bindings.utils.SingleLiveEvent


open class BaseViewModel : ViewModel() {

    protected val mMessageNotifier = SingleLiveEvent<MessageWrapper>()

    protected var mItemClickNotifier: SingleLiveEvent<ClickItemWrapper> = SingleLiveEvent()

    open fun handlerError(throwable: Throwable) {
        throwable.printStackTrace()
    }

    open fun getMessageDelegate() = mMessageNotifier

    open fun getItemClickDelegate() = mItemClickNotifier
}