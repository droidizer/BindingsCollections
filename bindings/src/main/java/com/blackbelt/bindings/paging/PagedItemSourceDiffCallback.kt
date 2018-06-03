package com.blackbelt.bindings.paging

import android.support.v7.util.DiffUtil

class PagedItemSourceDiffCallback<T> : DiffUtil.ItemCallback<T>() where T : PagedItem {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = oldItem.getItemId() == newItem.getItemId()

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = oldItem == newItem
}
