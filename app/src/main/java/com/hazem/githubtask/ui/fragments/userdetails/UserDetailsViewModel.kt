package com.hazem.githubtask.ui.fragments.userdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.hazem.githubtask.data.network.response.RepoDetails
import com.hazem.githubtask.data.repository.RepositoryApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserDetailsViewModel(private val repository: RepositoryApi) : ViewModel() {

    companion object {
        private const val VISIBLE_THRESHOLD = 3
    }


    internal suspend fun requestMoreRepos() = GlobalScope.async {
        return@async repository.requestMoreRepos()
    }


    internal val loadMoreLiveData = repository.loadMoreLiveData


    internal suspend fun getUserRepos(userName: String) = GlobalScope.async {
        return@async repository.getUserRepos(userName)
    }

    internal suspend fun deleteOldData() = GlobalScope.async {
        return@async repository.deleteOldUserRepos()
    }

    fun listScrolled(
        visibleItemCount: Int,
        firstVisibleItemPosition: Int,
        totalItemCount: Int
    ) = GlobalScope.launch {
        if (visibleItemCount + firstVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount ) {
            repository.requestMoreRepos()
        }
    }


}
