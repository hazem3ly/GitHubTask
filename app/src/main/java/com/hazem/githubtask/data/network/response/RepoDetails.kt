package com.hazem.githubtask.data.network.response

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_repos")
data class RepoDetails(
    @PrimaryKey(autoGenerate = true)
        @SerializedName("id") var id: Int?,
    @SerializedName("name") var name: String?,
    @Embedded(prefix = "user_")
        @SerializedName("owner") var owner: Owner?,
    @SerializedName("html_url") var html_url: String?,
    @SerializedName("description") var description: String?,
    @SerializedName("language") var language: String?,
    @SerializedName("forks_count") var forks_count: Int?,
    @SerializedName("stargazers_count") var stargazers_count: Int?,
    @SerializedName("created_at") var created_at: String?
)