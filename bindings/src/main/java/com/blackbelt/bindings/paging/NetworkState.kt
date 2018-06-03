package com.blackbelt.bindings.paging

enum class NetworkStateInternal {
    RUNNING,
    SUCCESS,
    FAILED
}

data class NetworkState constructor(private val status: NetworkStateInternal,
                                    val throwable: Throwable? = null) {

    fun isLoading(): Boolean = status == NetworkStateInternal.RUNNING
    fun isError(): Boolean = status == NetworkStateInternal.FAILED
    fun isLoaded(): Boolean = status == NetworkStateInternal.SUCCESS

    companion object {
        val LOADING = NetworkState(NetworkStateInternal.RUNNING)
        val LOADED = NetworkState(NetworkStateInternal.SUCCESS)
        fun withError(throwable: Throwable?) = NetworkState(NetworkStateInternal.FAILED, throwable)
    }
}

