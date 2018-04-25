package com.blackbelt.bindings.notifications

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.content.DialogInterface
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.blackbelt.bindings.R
import java.lang.ref.WeakReference


class MessageDelegate : ViewModel() {

    private var mToast: Toast? = null

    private var mSnackbar: Snackbar? = null

    private var mActivity: WeakReference<Activity>? = null

    private var mDialog: AlertDialog? = null

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
                    showDialog(activity, messageWrapper)
                }
            }
        } catch (e: Exception) {
        }

    }

    private fun showDialog(activity: Activity, messageWrapper: MessageWrapper) {

        if (activity.isFinishing) {
            return
        }

        mDialog?.dismiss()

        try {

            val builder = when (messageWrapper.dialogStyleId > 0) {
                true -> AlertDialog.Builder(activity, messageWrapper.dialogStyleId)
                false -> AlertDialog.Builder(activity)
            }
            if (messageWrapper.titleId > 0) {
                builder.setTitle(messageWrapper.titleId)
            } else {
                builder.setTitle(messageWrapper.title)
            }
            if (messageWrapper.messageId > 0) {
                builder.setMessage(messageWrapper.messageId)
            } else {
                builder.setMessage(messageWrapper.message)
            }
            if (messageWrapper.positiveButtonId > 0) {
                builder.setPositiveButton(messageWrapper.positiveButtonId, messageWrapper.positiveOnClickListener)
            }
            if (messageWrapper.negativeButtonId > 0) {
                builder.setNegativeButton(messageWrapper.negativeButtonId, messageWrapper.negativeOnClickListener)
            }

            mDialog = builder.show()

        } catch (e: Exception) {
        }
    }

    override fun onCleared() {
        super.onCleared()
        mDialog?.dismiss()
        mDialog = null
        mActivity?.clear()
        mActivity = null

        mToast?.cancel()
        mToast = null

        mSnackbar?.dismiss()
        mSnackbar = null
    }
}