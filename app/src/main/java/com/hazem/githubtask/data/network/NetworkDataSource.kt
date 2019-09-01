package com.hazem.githubtask.data.network

import androidx.lifecycle.LiveData
import com.hazem.githubtask.data.network.response.RepoDetails
import retrofit2.Response

interface NetworkDataSource {
    val userRepos: LiveData<Response<List<RepoDetails>>>
    suspend fun userRepos(userName: String, page: Int)
}