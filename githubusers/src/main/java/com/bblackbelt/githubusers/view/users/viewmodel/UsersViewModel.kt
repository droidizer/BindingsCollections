package com.bblackbelt.githubusers.view.users.viewmodel

import android.arch.lifecycle.*
import android.arch.paging.PagedList
import android.content.res.Resources
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bblackbelt.githubusers.R
import com.bblackbelt.githubusers.api.model.User
import com.bblackbelt.githubusers.repository.users.IUserDataRepository
import com.bblackbelt.githubusers.BR
import com.blackbelt.bindings.notifications.ClickItemWrapper
import com.blackbelt.bindings.notifications.MessageWrapper
import com.blackbelt.bindings.paging.NetworkState
import com.blackbelt.bindings.recyclerviewbindings.AndroidItemBinder
import com.blackbelt.bindings.recyclerviewbindings.ItemClickListener
import com.blackbelt.bindings.viewmodel.BaseViewModel
import javax.inject.Inject


class UsersViewModel(private val usersDataRepository: IUserDataRepository) : BaseViewModel() {

    private val pageSize = MutableLiveData<Int>()
    private val repoResult =
            Transformations.map(pageSize, { usersDataRepository.getUsers(it) })

    val networkStateLayout = R.layout.network_state_item

    val itemClick = object : ItemClickListener {
        override fun onItemClicked(view: View, item: Any?) {
            item?.let {
                when (it) {
                    is NetworkState -> {
                        if (it.isError()) {
                            retry()
                        }
                    }
                    is User -> {
                        val listItem = item as? User ?: return
                        mMessageNotifier.value = MessageWrapper.withSnackBar(listItem.login)
                    }
                }
            }
        }
    }

    val itemDecoration: RecyclerView.ItemDecoration by lazy {
        val margin: Int = (Resources.getSystem().displayMetrics.density * 4f).toInt()
        val lateralMargin: Int = (Resources.getSystem().displayMetrics.density * 16f).toInt()
        object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                outRect?.bottom = margin
                outRect?.top = margin
                outRect?.left = lateralMargin
                outRect?.right = lateralMargin
            }
        }
    }

    init {
        pageSize.value = 50
    }

    val templates: Map<Class<*>, AndroidItemBinder> by lazy {
        mapOf<Class<*>, AndroidItemBinder>(User::class.java to AndroidItemBinder(R.layout.user_item, BR.userItem),
                NetworkState::class.java to AndroidItemBinder(R.layout.network_state_item, BR.networkState))
    }

    val users = Transformations.switchMap(repoResult, { it.pagedList })!!

    val networkState: LiveData<NetworkState> = Transformations.switchMap(repoResult, { it.networkState })!!

    class Factory @Inject constructor(val usersDataRepository: IUserDataRepository) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = UsersViewModel(usersDataRepository) as T
    }

    fun retry() {
        repoResult?.value?.retry?.invoke()
    }
}