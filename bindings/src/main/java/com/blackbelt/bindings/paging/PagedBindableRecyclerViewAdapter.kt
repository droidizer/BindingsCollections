package com.blackbelt.bindings.paging

import android.arch.paging.PagedList
import android.arch.paging.PagedListAdapter
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.ViewGroup
import com.blackbelt.bindings.recyclerviewbindings.AndroidItemBinder


class PagedBindableRecyclerViewAdapter<T>(
        private val itemBinder: Map<Class<*>, AndroidItemBinder>)
    : PagedListAdapter<T, PagedBindableViewHolder>(PagedItemSourceDiffCallback<T>()) where T : PagedItem {

    private var networkState: NetworkState? = null

    private var networkStateLayoutId: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagedBindableViewHolder {
        if (isNetworkState(viewType)) {
            val binding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context),
                    viewType, parent, false)
            return NetworkStateViewHolder(binding)
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), viewType, parent, false)
        return PagedBindableViewHolder(binding)
    }

    fun isNetworkState(viewType: Int): Boolean = itemBinder[NetworkState::class.java]?.let {
        return it.layoutId == viewType
    } ?: false

    override fun onBindViewHolder(holder: PagedBindableViewHolder, position: Int) {
        if (holder is NetworkStateViewHolder) {
            val binder = itemBinder[NetworkState::class.java] ?: return
            holder.viewDataBinding.setVariable(binder.dataBindingVariable, networkState)
            holder.viewDataBinding.executePendingBindings()
            return
        }
        val item = getItem(position) ?: return
        val itemBinder = itemBinder[item.javaClass] ?: return
        holder.viewDataBinding.setVariable(itemBinder.dataBindingVariable, item)
        holder.viewDataBinding.executePendingBindings()
    }

    override fun getItemViewType(position: Int): Int {

        itemBinder[NetworkState::class.java]?.let {
            if (hasExtraRow() && itemCount - 1 == position) {
                return it.layoutId
            }
        }
        val item: T = getItem(position) ?: throw Exception("Got null item in dataset")
        val key = item.javaClass
        val dataBinder: AndroidItemBinder = itemBinder[key]
                ?: throw Exception("AndroidDataBinder not configured correctly $key")
        return dataBinder.layoutId
    }

    fun setNetworkStateLayout(@LayoutRes layoutId: Int?) {
        networkStateLayoutId = layoutId
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    override fun getItemCount(): Int {
        return super.getItemCount() +
                when (hasExtraRow()) {
                    true -> 1
                    false -> 0
                }
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }
}