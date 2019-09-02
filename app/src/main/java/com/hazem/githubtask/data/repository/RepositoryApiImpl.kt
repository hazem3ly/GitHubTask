package com.hazem.githubtask.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hazem.githubtask.data.db.RepoDetailsDao
import com.hazem.githubtask.data.model.RepoDetails
import com.hazem.githubtask.data.network.NetworkDataSource
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
    override val errorLiveData = MutableLiveData<String>()


    init {
        networkDataSource.apply {
            userRepos.observeForever { it ->
                isRequestInProgress = false
                loadMoreLiveData.postValue(false)
                // persist
                it?.let {
                    if (it.isSuccessful && it.body() != null) {
                        lastRequestedPage++
                        persistFetchedData(it)
                        if (it.body()?.isEmpty() == true)
                            listBecomeEmpty = true
                    }
                }
                if (it == null) errorLiveData.postValue("Error While Loading Data")
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
        getMoreUserRepos(userName)
    }

    override suspend fun getUserRepos(userName: String): LiveData<List<RepoDetails>> {
        this.userName = userName
        lastRequestedPage = 1
        isRequestInProgress = true
        return withContext(Dispatchers.IO) {
            networkDataSource.userRepos(userName, lastRequestedPage)
            return@withContext repoDetailsDao.getSavedRepos()
        }
    }

    suspend fun getMoreUserRepos(userName: String) {
        this.userName = userName
        isRequestInProgress = true
        withContext(Dispatchers.IO) {
            networkDataSource.userRepos(userName, lastRequestedPage)
        }
    }

}