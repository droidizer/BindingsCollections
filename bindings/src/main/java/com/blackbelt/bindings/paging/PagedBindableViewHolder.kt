package com.blackbelt.bindings.paging

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

open class PagedBindableViewHolder(val viewDataBinding: ViewDataBinding)
    : RecyclerView.ViewHolder(viewDataBinding.root)