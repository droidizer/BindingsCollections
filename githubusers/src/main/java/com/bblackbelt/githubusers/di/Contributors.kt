package com.bblackbelt.githubusers.di

import com.bblackbelt.githubusers.view.details.UserDetailsActivity
import com.bblackbelt.githubusers.view.users.UsersActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface Contributors {

    @ContributesAndroidInjector
    fun usersActivity() : UsersActivity

    @ContributesAndroidInjector
    fun userDetailsActivity() : UserDetailsActivity
}