package com.blackbelt.bindings.paging

import android.arch.paging.PagedList
import android.arch.paging.PagedListAdapter
import android.content.Context
import android.util.AttributeSet
import com.blackbelt.bindings.BaseBindableRecyclerView

class PagedBindableRecyclerView(context: Context, attrs: AttributeSet?)
    : BaseBindableRecyclerView(context, attrs) {

    override fun getDataSet(): List<Any>? =
            (adapter as? PagedBindableRecyclerViewAdapter<*>)?.currentList

    override fun getItemAtPosition(position: Int): Any? {
        val list = getDataSet()
        if (position >= list?.size ?: 0) {
            return (adapter as? PagedBindableRecyclerViewAdapter<*>)?.getNetworkState()
        }
        return list?.get(position)
    }

    fun <T : PagedItem> submitList(list: PagedList<T>?) {
        (adapter as? PagedBindableRecyclerViewAdapter<T>)?.submitList(list)
    }
}