package com.bblackbelt.githubusers.view.details

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.bblackbelt.githubusers.R
import com.bblackbelt.githubusers.api.model.UserDetails
import com.bblackbelt.githubusers.utils.setGitHubAvatar
import com.bblackbelt.githubusers.view.details.viewmodel.UserDetailsViewModel
import com.blackbelt.bindings.paging.NetworkState
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.user_details.*
import javax.inject.Inject

class UserDetailsActivity : AppCompatActivity() {

    companion object {
        val ID_KEY = "ID_KEY"
    }

    @Inject
    lateinit var mFactory: UserDetailsViewModel.Factory

    private val mViewModel: UserDetailsViewModel by lazy {
        ViewModelProviders.of(this, mFactory)[UserDetailsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_details)
        mViewModel.userLiveData.observe(this, Observer<UserDetails> {
            name.text = it?.name
            location.text = it?.location
            avatar.setGitHubAvatar(it?.avatarUrl)
            follower.text = resources.getString(R.string.followers, it?.followers.toString())
            following.text = resources.getString(R.string.following, it?.following.toString())
        })
        mViewModel.networkState.observe(this, Observer<NetworkState> {
          //  progress_bar.toVisibility(it == NetworkState.LOADING)
          // root.toVisibility(it == NetworkState.LOADED)
        })
        mViewModel.userId = intent.getStringExtra(ID_KEY)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra(ID_KEY)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
