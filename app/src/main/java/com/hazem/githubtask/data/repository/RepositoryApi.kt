package com.hazem.githubtask.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hazem.githubtask.data.model.RepoDetails

interface RepositoryApi {
    val loadMoreLiveData: MutableLiveData<Boolean>
    val errorLiveData: MutableLiveData<String>
    suspend fun deleteOldUserRepos()
    suspend fun requestMoreRepos()
    suspend fun getUserRepos(userName: String): LiveData<List<RepoDetails>>
}