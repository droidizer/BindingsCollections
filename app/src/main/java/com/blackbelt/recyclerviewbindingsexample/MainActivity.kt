package com.blackbelt.recyclerviewbindingsexample

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import com.blackbelt.bindings.activity.BaseBindingActivity
import com.blackbelt.bindings.notifications.ClickItemWrapper
import com.blackbelt.recyclerviewbindingsexample.viewmodel.MainViewModel

class MainActivity : BaseBindingActivity() {

    private val mFactory: MainViewModel.Factory by lazy {
        MainViewModel.Factory(resources)
    }

    private val mMainViewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, mFactory).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main, BR.mainViewModel, mMainViewModel)
    }

    override fun onItemClicked(itemWrapper: ClickItemWrapper) {
        Log.d("TEST", "TEST " + itemWrapper.additionalData)
    }
}
