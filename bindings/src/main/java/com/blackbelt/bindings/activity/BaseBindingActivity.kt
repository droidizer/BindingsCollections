package com.blackbelt.bindings.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import com.blackbelt.bindings.notifications.ClickItemWrapper
import com.blackbelt.bindings.notifications.MessageDelegate
import com.blackbelt.bindings.viewmodel.BaseViewModel

abstract class BaseBindingActivity : AppCompatActivity() {

    private var mViewModel: BaseViewModel? = null

    private val mMessageDelegate: MessageDelegate by lazy {
        ViewModelProviders.of(this).get(MessageDelegate::class.java)
    }

    private var mDataBinding: ViewDataBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMessageDelegate.setActivity(this)
    }

    fun setContentView(@LayoutRes layoutId: Int, brVariable: Int, viewModel: BaseViewModel) {
        mViewModel = viewModel
        mViewModel?.getMessageDelegate()?.observe(this, Observer { t -> mMessageDelegate.showMessage(t) })
        mViewModel?.getItemClickDelegate()?.observe(this, Observer { t -> this.handleClick(t) })

        mDataBinding = DataBindingUtil.inflate(layoutInflater, layoutId, null, false)
        mDataBinding?.setVariable(brVariable, viewModel)
        mDataBinding?.setLifecycleOwner(this)
        super.setContentView(mDataBinding?.root)
    }

    private fun handleClick(item: ClickItemWrapper?) {
        val itemClicked = item ?: return
        onItemClicked(itemClicked)
    }

    protected open fun onItemClicked(itemWrapper: ClickItemWrapper) {}

    override fun onDestroy() {
        super.onDestroy()
        mDataBinding?.let {
            it.setLifecycleOwner(null)
        }
    }
}