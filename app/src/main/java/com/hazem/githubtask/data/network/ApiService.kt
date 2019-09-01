package com.hazem.githubtask.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.hazem.githubtask.data.network.response.RepoDetails
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val BASE_URL = "https://api.github.com/"
const val REPO_OWNER_NAME = "JakeWharton"
const val REPO_PER_PAGE = 15

interface ApiService {

    //https://api.github.com/users/JakeWharton/repos
    @GET("users/{user_name}/repos")
    fun userRepos(
        @Path("user_name") userName: String = REPO_OWNER_NAME,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = REPO_PER_PAGE,
        @Query("sort") sort: String = "created",
        @Query("direction") direction: String = "asc"
    ): Deferred<Response<List<RepoDetails>>>

    companion object {
        operator fun invoke(connectivityInterceptor: ConnectivityInterceptor): ApiService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .addInterceptor(connectivityInterceptor)
                    .build()

            return Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BASE_URL) // TODO Don't Forget To Change BASE_URL
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiService::class.java)
        }
    }

}