package com.blackbelt.recyclerviewbindingsexample.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

data class ItemViewModel(private val _name: String) : ViewModel() {

    private val backedNname : MutableLiveData<String> = MutableLiveData()

    init {
        backedNname.value = _name
    }

    val name: LiveData<String>
        get() = backedNname

    fun setName(name: String) {
        backedNname.value = name
    }
}