package com.hazem.githubtask.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hazem.githubtask.data.network.response.RepoDetails

interface RepositoryApi {
    val loadMoreLiveData : MutableLiveData<Boolean>
    suspend fun deleteOldUserRepos()
    suspend fun requestMoreRepos()
    suspend fun getUserRepos(userName: String): LiveData<List<RepoDetails>>
}