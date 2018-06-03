package com.bblackbelt.githubusers.di

import com.bblackbelt.githubusers.GitHubApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [(AndroidSupportInjectionModule::class), (NetworkModule::class), (Contributors::class), (BindsModule::class)])
interface GitHubComponent {

    fun inject(app: GitHubApp)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: GitHubApp): Builder

        fun build(): GitHubComponent
    }
}