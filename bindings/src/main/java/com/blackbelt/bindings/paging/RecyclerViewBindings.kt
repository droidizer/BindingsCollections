package com.blackbelt.bindings.paging

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import com.blackbelt.bindings.recyclerviewbindings.AndroidItemBinder

@BindingAdapter("submitList")
fun <T> submitList(view: PagedBindableRecyclerView, items: LiveData<PagedList<T>>?) where T : PagedItem {
    view.submitList(items?.value)
}

@BindingAdapter("objectTemplates")
fun <T : PagedItem> setItemViewBinder(recyclerView: RecyclerView, itemViewMapper: Map<Class<*>, AndroidItemBinder>) {
    if (recyclerView.adapter != null) {
        return
    }
    val adapter = PagedBindableRecyclerViewAdapter<T>(itemViewMapper)
    recyclerView.adapter = adapter
}

@BindingAdapter("networkState")
fun setNetworkState(recyclerView: RecyclerView, networkState: NetworkState?) {
    (recyclerView.adapter as? PagedBindableRecyclerViewAdapter<*>)
            ?.setNetworkState(networkState)
}

@BindingAdapter("networkState")
fun setNetworkState(recyclerView: RecyclerView, networkState: LiveData<NetworkState>?) {
    (recyclerView.adapter as? PagedBindableRecyclerViewAdapter<*>)
            ?.setNetworkState(networkState?.value)
}

@BindingAdapter("networkStateLayout")
fun setNetworkStateLayout(recyclerView: RecyclerView, networkState: Int?) {
    (recyclerView.adapter as? PagedBindableRecyclerViewAdapter<*>)
            ?.setNetworkStateLayout(networkState)
}