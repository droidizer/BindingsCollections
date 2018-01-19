package com.blackbelt.bindings.recyclerviewbindings

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

class BindableViewHolder(viewDataBinding: ViewDataBinding) : RecyclerView.ViewHolder(viewDataBinding.root) {
    val mViewDataBinding = viewDataBinding
}