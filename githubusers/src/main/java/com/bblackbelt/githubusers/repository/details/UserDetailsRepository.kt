package com.bblackbelt.githubusers.repository.details


import com.bblackbelt.githubusers.api.GitHubService
import com.bblackbelt.githubusers.repository.details.IUserDetailsRepository
import com.bblackbelt.githubusers.api.model.UserDetails
import io.reactivex.Observable
import javax.inject.Inject

class UserDetailsRepository @Inject constructor(private val gitHubService: GitHubService) : IUserDetailsRepository {

    override fun getUserDetails(user: String): Observable<UserDetails> {
        return gitHubService.getUserDetails(user)
    }
}