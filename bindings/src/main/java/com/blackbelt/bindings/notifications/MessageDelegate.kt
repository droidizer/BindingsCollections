package com.blackbelt.bindings.notifications

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.support.design.widget.Snackbar
import android.widget.Toast
import java.lang.ref.WeakReference


class MessageDelegate : ViewModel() {

    private var mToast: Toast? = null

    private var mSnackbar: Snackbar? = null

    private var mActivity: WeakReference<Activity>? = null

    fun setActivity(activity: Activity) {
        mActivity = WeakReference(activity)
    }

    fun showMessage(messageWrapper: MessageWrapper?) {
        try {
            val activity = mActivity?.get() ?: return
            val type = messageWrapper?.type
            when (type) {
                MessageWrapper.Type.TOAST -> {
                    mToast?.cancel()
                    val theMessage: String? =
                            when {
                                messageWrapper.message != null -> messageWrapper.message
                                messageWrapper.messageId > 0 -> activity.getString(messageWrapper.messageId)
                                else -> null
                            }
                    val message = theMessage ?: return
                    mToast = Toast.makeText(activity, message, messageWrapper.duration)
                    mToast?.show()
                }
                MessageWrapper.Type.SNACKBAR -> {
                    mSnackbar?.dismiss()
                    val theMessage: String? =
                            when {
                                messageWrapper.message != null -> messageWrapper.message
                                messageWrapper.messageId > 0 -> activity.getString(messageWrapper.messageId)
                                else -> null
                            }
                    val message = theMessage ?: return
                    mSnackbar = Snackbar
                            .make(activity.findViewById(android.R.id.content),
                                    message,
                                    messageWrapper.duration)
                    mSnackbar?.show()
                }
                MessageWrapper.Type.DIALOG -> {
                }
            }
        } catch (e: Exception) {
        }

    }

    override fun onCleared() {
        super.onCleared()

        mActivity?.clear()
        mActivity = null

        mToast?.cancel()
        mToast = null

        mSnackbar?.dismiss()
        mSnackbar = null
    }
}