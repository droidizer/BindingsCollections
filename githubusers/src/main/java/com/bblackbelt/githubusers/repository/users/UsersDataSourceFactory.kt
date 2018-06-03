package com.bblackbelt.githubusers.repository.users

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.bblackbelt.githubusers.api.model.User
import javax.inject.Inject

class UsersDataSourceFactory @Inject constructor(private val source: UsersPageKeyedDataSource) : DataSource.Factory<Int, User>() {
    val sourceLiveData = MutableLiveData<UsersPageKeyedDataSource>()

    override fun create(): DataSource<Int, User> {
        sourceLiveData.postValue(source)
        return source
    }
}