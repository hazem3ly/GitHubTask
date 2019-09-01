package com.hazem.githubtask

import android.app.Application
import com.hazem.githubtask.data.db.ReposDatabase
import com.hazem.githubtask.data.network.*
import com.hazem.githubtask.data.repository.RepositoryApi
import com.hazem.githubtask.data.repository.RepositoryApiImpl
import com.hazem.githubtask.ui.fragments.userdetails.UserDetailsViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class App : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@App))
        bind() from singleton { ReposDatabase(instance()) }
        bind() from singleton { instance<ReposDatabase>().repoDetailsDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { ApiService(instance()) }
        bind<NetworkDataSource>() with singleton { NetworkDataSourceImpl(instance()) }
        bind<RepositoryApi>() with singleton { RepositoryApiImpl(instance(), instance()) }
        bind() from provider { UserDetailsViewModelFactory(instance()) }
    }

}