package com.bblackbelt.githubusers

import android.app.Activity
import android.app.Application
import android.content.Context
import com.bblackbelt.githubusers.di.DaggerGitHubComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject
import android.support.multidex.MultiDex



open class GitHubApp : Application(), HasActivityInjector {

    @Inject
    lateinit var mAndroidDispatchingInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerGitHubComponent
                .builder()
                .application(this)
                .build()
                .inject(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = mAndroidDispatchingInjector
}