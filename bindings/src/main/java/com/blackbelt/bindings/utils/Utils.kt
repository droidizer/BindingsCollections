package com.blackbelt.bindings.utils

import android.databinding.BindingAdapter
import android.view.View

@BindingAdapter("toVisibility")
fun setVisibility(view: View, show: Boolean) {
    view.visibility = when (show) {
        true -> View.VISIBLE
        false -> View.GONE
    }
}