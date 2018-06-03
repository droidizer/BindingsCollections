package com.bblackbelt.githubusers.di

import com.bblackbelt.githubusers.repository.details.IUserDetailsRepository
import com.bblackbelt.githubusers.repository.details.UserDetailsRepository
import com.bblackbelt.githubusers.repository.users.IUserDataRepository
import com.bblackbelt.githubusers.repository.users.UsersDataRepository
import dagger.Binds
import dagger.Module

@Module
abstract class BindsModule {

    @Binds
    abstract fun bindsUserDataRepository(userData: UsersDataRepository): IUserDataRepository

    @Binds
    abstract fun bindsUserDetailsRepository(userData: UserDetailsRepository): IUserDetailsRepository
}