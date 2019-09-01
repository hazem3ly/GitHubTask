package com.hazem.githubtask.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hazem.githubtask.data.network.response.RepoDetails
import retrofit2.Response
import java.io.IOException

class NetworkDataSourceImpl(
    private val apiService: ApiService
) : NetworkDataSource {

    private val _userRepos = MutableLiveData<Response<List<RepoDetails>>>()
    override val userRepos: LiveData<Response<List<RepoDetails>>>
        get() = _userRepos

    override suspend fun userRepos(userName: String, page: Int) {
        try {
            val repo = apiService.userRepos(userName,page).await()
            _userRepos.postValue(repo)
        } catch (e: IOException) {
            Log.e("Connectivity", "No internet connection.", e)
            _userRepos.postValue(null)
        }
    }
}