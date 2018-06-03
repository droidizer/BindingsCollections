package com.bblackbelt.githubusers.utils

import android.databinding.BindingAdapter
import android.graphics.BitmapFactory
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.widget.ImageView
import com.bblackbelt.githubusers.R
import com.bblackbelt.githubusers.utils.RoundedTransformation
import com.squareup.picasso.Picasso

@BindingAdapter("srcUrl")
fun ImageView.setGitHubAvatar(url: String?) {

    val placeHolder = BitmapFactory.decodeResource(resources, R.drawable.placeholder)
    val roundedPlaceholder = RoundedBitmapDrawableFactory.create(resources, placeHolder)
    roundedPlaceholder.isCircular = true

    if (url.isNullOrEmpty()) {
        setImageDrawable(roundedPlaceholder)
        return
    }

    Picasso.get()
            .load(url)
            .placeholder(roundedPlaceholder)
            .fit()
            .centerCrop()
            .transform(RoundedTransformation(url)).into(this)
}
