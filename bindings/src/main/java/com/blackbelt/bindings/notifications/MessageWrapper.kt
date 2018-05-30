package com.blackbelt.bindings.notifications

import android.content.DialogInterface
import android.support.annotation.IntDef
import android.support.design.widget.Snackbar
import android.widget.Toast
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

class MessageWrapper private constructor() {

    var titleId = -1
        private set

    var messageId = -1
        private set

    var positiveButtonId = -1
        private set

    var negativeButtonId = -1
        private set

    var positiveButton: String? = null
        private set

    var pegativeButton: String? = null
        private set

    var title: String? = null
        private set

    var message: String? = null
        private set

    var type = Type.TOAST
        private set

    var duration: Int = 0
        private set

    var dialogStyleId: Int = 0
        private set

    private var mPositiveOnClickListener: DialogInterface.OnClickListener? = null

    private var mNegativeOnClickListener: DialogInterface.OnClickListener? = null

    val positiveOnClickListener: DialogInterface.OnClickListener?
        get() = mPositiveOnClickListener

    val negativeOnClickListener: DialogInterface.OnClickListener?
        get() = mNegativeOnClickListener

    @IntDef(LENGTH_INDEFINITE, LENGTH_SHORT, LENGTH_LONG)
    @Retention(RetentionPolicy.SOURCE)
    annotation class Duration

    enum class Type {
        TOAST,
        DIALOG,
        SNACKBAR
    }


    companion object {

        const val LENGTH_INDEFINITE = Snackbar.LENGTH_INDEFINITE

        const val LENGTH_SHORT = Snackbar.LENGTH_SHORT

        const val LENGTH_LONG = Snackbar.LENGTH_LONG

        fun withSnackBar(messageId: Int, @Duration duration: Int = LENGTH_SHORT): MessageWrapper {
            val messageWrapper = MessageWrapper()
            messageWrapper.messageId = messageId
            messageWrapper.type = Type.SNACKBAR
            messageWrapper.duration = duration
            return messageWrapper
        }

        fun withToast(messageId: Int, @Duration duration: Int = LENGTH_SHORT): MessageWrapper {
            val messageWrapper = MessageWrapper()
            messageWrapper.messageId = messageId
            messageWrapper.type = Type.TOAST
            messageWrapper.duration = if (duration == LENGTH_LONG) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
            return messageWrapper
        }

        fun withSnackBar(message: String?, @Duration duration: Int = LENGTH_SHORT): MessageWrapper {
            val messageWrapper = MessageWrapper()
            messageWrapper.message = message
            messageWrapper.type = Type.SNACKBAR
            messageWrapper.duration = duration
            return messageWrapper
        }

        fun withToast(message: String, @Duration duration: Int = LENGTH_SHORT): MessageWrapper {
            val messageWrapper = MessageWrapper()
            messageWrapper.message = message
            messageWrapper.type = Type.TOAST
            messageWrapper.duration = if (duration == LENGTH_LONG) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
            return messageWrapper
        }

        fun withDialog(titleId: Int, messageId: Int,
                       dialogStyle : Int = 0,
                       positiveButtonId: Int = 0, negativeButtonId: Int = 0,
                       positiveOnClickListener: DialogInterface.OnClickListener? = null,
                       negativeOnClickListener: DialogInterface.OnClickListener? = null): MessageWrapper {
            val messageWrapper = MessageWrapper()
            messageWrapper.titleId = titleId
            messageWrapper.messageId = messageId
            messageWrapper.dialogStyleId = dialogStyle
            messageWrapper.positiveButtonId = positiveButtonId
            messageWrapper.negativeButtonId = negativeButtonId
            messageWrapper.type = Type.DIALOG
            messageWrapper.mPositiveOnClickListener = positiveOnClickListener
            messageWrapper.mNegativeOnClickListener = negativeOnClickListener
            return messageWrapper
        }

        fun withDialog(title: String, message: String): MessageWrapper {
            val messageWrapper = MessageWrapper()
            messageWrapper.message = message
            messageWrapper.title = title
            messageWrapper.type = Type.DIALOG
            return messageWrapper
        }
    }
}