package com.hazem.githubtask.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hazem.githubtask.data.network.response.RepoDetails

@Dao
interface RepoDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(futureWeatherEntries: List<RepoDetails>)

    @Query("select * from user_repos")
    fun getSavedRepos(): LiveData<List<RepoDetails>>

    @Query("delete from user_repos")
    fun deleteOldEntries()
}