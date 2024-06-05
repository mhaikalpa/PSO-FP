package com.dicoding.githubuserkresna2.api

import com.dicoding.githubuserkresna2.BuildConfig
import com.dicoding.githubuserkresna2.data.SearchResponse
import com.dicoding.githubuserkresna2.data.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    @Headers("Authorization: token $API_TOKEN", "UserResponse-Agent: request")
    suspend fun getListUsersAsync(): ArrayList<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token $API_TOKEN", "UserResponse-Agent: request")
    suspend fun getDetailUserAsync(@Path("username") username: String): UserResponse

    @GET("search/users")
    @Headers("Authorization: token $API_TOKEN", "UserResponse-Agent: request")
    fun getUserBySearch(@Query("q") username: String): Call<SearchResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token $API_TOKEN", "UserResponse-Agent: request")
    suspend fun getListFollowers(@Path("username") username: String): ArrayList<UserResponse>

    @GET("users/{username}/following")
    @Headers("Authorization: token $API_TOKEN", "UserResponse-Agent: request")
    suspend fun getListFollowing(@Path("username") username: String): ArrayList<UserResponse>

    companion object {
        private const val API_TOKEN = BuildConfig.API_GITHUB_TOKEN
    }
}
