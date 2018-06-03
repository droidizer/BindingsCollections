package com.bblackbelt.githubusers.api

import com.bblackbelt.githubusers.api.model.User
import com.bblackbelt.githubusers.api.model.UserDetails
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubService {
    @GET("users")
    fun getUsers(@Query("since") page: Int = 0, @Query("per_page") pageSize: Int = 25): Observable<List<User>>

    @GET("users/{id}")
    fun getUserDetails(@Path("id") id: String): Observable<UserDetails>
}