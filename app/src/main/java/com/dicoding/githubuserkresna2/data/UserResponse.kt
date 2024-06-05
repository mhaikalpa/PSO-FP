package com.dicoding.githubuserkresna2.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(
    val id: Int = 0,
    val name: String?,
    val login: String?,
    @field:SerializedName("avatar_url")
    val avatarUrl: String?,
    val bio: String?,
    @field:SerializedName("public_repos")
    val publicRepo: String?,
    val followers: String?,
    val following: String?,
    @field:SerializedName("followers_url")
    val followersUrl: String?,
    @field:SerializedName("following_url")
    val followingUrl: String?,
    @field:SerializedName("html_url")
    val htmlUrl: String?
) : Parcelable