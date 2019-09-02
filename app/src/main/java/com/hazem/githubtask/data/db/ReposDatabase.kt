package com.hazem.githubtask.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hazem.githubtask.data.model.RepoDetails

@Database(
        entities = [RepoDetails::class],
        version = 1
)
abstract class ReposDatabase : RoomDatabase() {

    abstract fun repoDetailsDao(): RepoDetailsDao

    companion object {
        @Volatile
        private var instance: ReposDatabase? = null

        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
                context.applicationContext,
                ReposDatabase::class.java,
                "repos.db"
        ).build()
    }

}