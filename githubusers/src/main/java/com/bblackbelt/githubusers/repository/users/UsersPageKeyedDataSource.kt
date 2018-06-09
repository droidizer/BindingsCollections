package com.bblackbelt.githubusers.repository.users

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import com.bblackbelt.githubusers.api.GitHubService
import com.bblackbelt.githubusers.api.model.User
import com.blackbelt.bindings.paging.NetworkState
import io.reactivex.disposables.Disposables
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class UsersPageKeyedDataSource @Inject constructor(private val gitHubService: GitHubService) : PageKeyedDataSource<Int, User>() {

    private var failedFunction: (() -> Any)? = null

    private var mCurrentPageDisposable = Disposables.disposed()

    val networkState = MutableLiveData<NetworkState>()

    fun retry() {
        val prevFailedFunction = failedFunction
        failedFunction = null
        prevFailedFunction?.invoke()
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, User>) {
        networkState.postValue(NetworkState.LOADING)
        mCurrentPageDisposable.dispose()
        mCurrentPageDisposable =
                gitHubService.getUsers(0, params.requestedLoadSize)
                        .subscribe({
                            networkState.postValue(NetworkState.LOADED)
                            callback.onResult(it, it.first().id, it.last().id)
                        }, {
                            failedFunction = {
                                loadInitial(params, callback)
                            }
                            networkState.postValue(NetworkState.withError(it))
                        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        networkState.postValue(NetworkState.LOADING)
        mCurrentPageDisposable.dispose()
        mCurrentPageDisposable =
                gitHubService.getUsers(params.key, params.requestedLoadSize)
                        .subscribe({
                            callback.onResult(it, it.last().id)
                            networkState.postValue(NetworkState.LOADED)
                        }, {
                            failedFunction = {
                                loadAfter(params, callback)
                            }
                            networkState.postValue(NetworkState.withError(it))
                        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
    }

}