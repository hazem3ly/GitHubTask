package com.hazem.githubtask.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hazem.githubtask.data.db.RepoDetailsDao
import com.hazem.githubtask.data.network.NetworkDataSource
import com.hazem.githubtask.data.network.response.RepoDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class RepositoryApiImpl(
    private val networkDataSource: NetworkDataSource,
    private val repoDetailsDao: RepoDetailsDao
) : RepositoryApi {
    private var userName = ""
    private var lastRequestedPage = 1
    private var isRequestInProgress = false
    private var listBecomeEmpty = false

    override val loadMoreLiveData = MutableLiveData<Boolean>()


    init {
        networkDataSource.apply {
            userRepos.observeForever { it ->
                lastRequestedPage++
                isRequestInProgress = false
                loadMoreLiveData.postValue(false)
                // persist
                it?.let {
                    if (it.isSuccessful && it.body() != null) {
                        persistFetchedData(it)
                        if (it.body()?.isEmpty() == true)
                            listBecomeEmpty = true
                    }
                }
            }
        }
    }

    override suspend fun deleteOldUserRepos() {
        repoDetailsDao.deleteOldEntries()
    }

    private fun persistFetchedData(response: Response<List<RepoDetails>>) {
        GlobalScope.launch(Dispatchers.IO) {
            response.body()?.let { repoDetailsDao.insert(it) }
        }
    }

    override suspend fun requestMoreRepos() {
        if (listBecomeEmpty) return
        if (isRequestInProgress) return
        isRequestInProgress = true
        loadMoreLiveData.postValue(true)
        getUserRepos(userName)
    }

    override suspend fun getUserRepos(userName: String): LiveData<List<RepoDetails>> {
        this.userName = userName
        return withContext(Dispatchers.IO) {
            networkDataSource.userRepos(userName, lastRequestedPage)
            return@withContext repoDetailsDao.getSavedRepos()
        }
    }

}