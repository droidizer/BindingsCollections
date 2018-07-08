package com.blackbelt.recyclerviewbindingsexample.viewmodel

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField

data class ItemViewModel(private val _name: String) : ViewModel() {

    private val backedNname: ObservableField<String> = ObservableField()

    init {
        backedNname.set(_name)
    }

    val name: String?
        get() = backedNname.get()

    fun setName(name: String) {
        backedNname.set(name)
    }
}