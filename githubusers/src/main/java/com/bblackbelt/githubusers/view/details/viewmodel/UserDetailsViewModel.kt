package com.bblackbelt.githubusers.view.details.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.bblackbelt.githubusers.api.model.UserDetails
import com.bblackbelt.githubusers.repository.details.IUserDetailsRepository
import com.blackbelt.bindings.paging.NetworkState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import javax.inject.Inject

class UserDetailsViewModel(private val userDetailsRepository: IUserDetailsRepository) : ViewModel() {

    var mDetailsDisposable = Disposables.disposed()

    val userLiveData = MutableLiveData<UserDetails>()

    val networkState = MutableLiveData<NetworkState>()

    var userId: String? = null
        set(value) {
            field = value
            if (value.isNullOrEmpty()) {
                return
            }
            networkState.postValue(NetworkState.LOADING)
            mDetailsDisposable =
                    userDetailsRepository.getUserDetails(value!!)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                userLiveData.value = it
                                networkState.postValue(NetworkState.LOADED)
                            }, {})
        }

    override fun onCleared() {
        super.onCleared()
        mDetailsDisposable.dispose()
    }

    class Factory @Inject constructor(private val usersDetailsRepository: IUserDetailsRepository) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = UserDetailsViewModel(usersDetailsRepository) as T
    }
}