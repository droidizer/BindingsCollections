package com.bblackbelt.githubusers.repository.users

import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.support.annotation.MainThread
import com.bblackbelt.githubusers.api.model.User
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject


interface IUserDataRepository {
    fun getUsers(pageSize: Int = 25): ViewWrapper<User>
}

class UsersDataRepository @Inject constructor(private val factory: UsersDataSourceFactory) : IUserDataRepository {

    @MainThread
    override fun getUsers(pageSize: Int): ViewWrapper<User> {

        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(5)
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize)
                .build()

        return ViewWrapper(
                LivePagedListBuilder(factory, config)
                       // .setFetchExecutor { AndroidSchedulers.mainThread() }
                        .build(),
                Transformations.switchMap(factory.sourceLiveData, {
                    it.networkState
                }),
                { factory.sourceLiveData.value?.retry() }
        )
    }
}