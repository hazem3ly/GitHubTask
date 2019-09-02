package com.hazem.githubtask.ui.fragments.userdetails

import androidx.lifecycle.ViewModel
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
    internal val errorLiveData = repository.errorLiveData


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
        if ((visibleItemCount + firstVisibleItemPosition + VISIBLE_THRESHOLD) >= totalItemCount
            && firstVisibleItemPosition >= 0
            && totalItemCount >= 15
        ) {
            repository.requestMoreRepos()
        }
    }


}
