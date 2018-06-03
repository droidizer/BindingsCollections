package com.bblackbelt.githubusers.repository.details

import com.bblackbelt.githubusers.api.model.UserDetails
import io.reactivex.Observable


interface IUserDetailsRepository {
    fun getUserDetails(user: String): Observable<UserDetails>
}