package com.blackbelt.bindings.fragment

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blackbelt.bindings.notifications.ClickItemWrapper
import com.blackbelt.bindings.notifications.MessageDelegate
import com.blackbelt.bindings.viewmodel.BaseViewModel


abstract class BaseBindingFragment : Fragment() {

    protected var mViewModel: BaseViewModel? = null

    private val mMessageDelegate: MessageDelegate by lazy {
        ViewModelProviders.of(this).get(MessageDelegate::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMessageDelegate.setActivity(activity as Activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceStat: Bundle?, @LayoutRes layoutResId: Int,
                     bindingVariable: Int,
                     androidBaseViewModel: BaseViewModel): View? {

        mViewModel = androidBaseViewModel

        lifecycle.addObserver(androidBaseViewModel)

        mViewModel?.getMessageDelegate()?.observe(this, Observer { t -> mMessageDelegate.showMessage(t) })
        mViewModel?.getItemClickDelegate()?.observe(this, Observer { t -> this.handleClick(t) })

        val dataBiding: ViewDataBinding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        dataBiding.setVariable(bindingVariable, mViewModel)
        return dataBiding.root
    }

    private fun handleClick(item: ClickItemWrapper?) {
        val itemClicked = item ?: return
        onItemClicked(itemClicked)
    }

    protected open fun onItemClicked(itemWrapper: ClickItemWrapper) {}

    override fun onDestroy() {
        super.onDestroy()
        mViewModel?.let {
            lifecycle.removeObserver(mViewModel as BaseViewModel)
        }
    }
}