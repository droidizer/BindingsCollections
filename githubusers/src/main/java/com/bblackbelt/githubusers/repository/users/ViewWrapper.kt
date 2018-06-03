package com.bblackbelt.githubusers.repository.users

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList
import com.blackbelt.bindings.paging.NetworkState
import com.blackbelt.bindings.paging.PagedItem

data class ViewWrapper<T : PagedItem>(
        val pagedList: LiveData<PagedList<T>>,
        val networkState: LiveData<NetworkState>,
        val retry: () -> Unit)